package frc.bdlib.driver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.bdlib.misc.BDConstants.JoystickConstants.JoystickAxisID;
import frc.bdlib.misc.BDConstants.JoystickConstants.JoystickButtonID;
import frc.bdlib.misc.BDConstants.JoystickConstants.JoystickVariant;

public class ControllerAIO extends GenericHID {
    private JoystickVariant joystick_type;
    private Map<JoystickButtonID, JoystickButton> actual_buttons = new HashMap<>();

    public ControllerAIO(final int port) {
        super(port);
        Optional<JoystickVariant> found = JoystickVariant.findJoy(DriverStation.getJoystickName(super.getPort()));
        if (found.isPresent()) {
            joystick_type = found.get();
            System.out.println("hello");
        } else {
            joystick_type = JoystickVariant.XBOX;
        }

        for (JoystickButtonID id: JoystickButtonID.values()) {
            actual_buttons.put(id, new JoystickButton(this, joystick_type.getButton(id)));
        }

    }

    public JoystickButton getJoystickButton(JoystickButtonID id) {
        return actual_buttons.get(id);
    }

    public ToggleBooleanSupplier getToggleBooleanSupplier(JoystickButtonID id, double debounce) {
        return new ToggleBooleanSupplier(actual_buttons.get(id), debounce);
    }
 
    public JoystickAxisAIO getAxis(JoystickAxisID id, double deadzone) {
        return new JoystickAxisAIO(this, id, deadzone);
    }

    public JoystickAxisAIO getAxis(JoystickAxisID id, DoubleUnaryOperator line_function, double deadzone) {
        return new JoystickAxisAIO(this, id, line_function, deadzone);
    }

    public JoystickAxisAIO getAxis(JoystickAxisID id, DoubleUnaryOperator line_function) {
        return new JoystickAxisAIO(this, id, line_function);
    }

    public JoystickAxisAIO getAxis(JoystickAxisID id) {
        return new JoystickAxisAIO(this, id);
    }

    public JoystickVariant getVariant() {
        return joystick_type;
    }

    public boolean canRumble() {
        return joystick_type.getCanRumble();
    }
}
