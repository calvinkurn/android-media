package ai.advance.liveness.lib;

/**
 * createTime:2019-11-07
 *
 * @author fan.zhang@advance.ai
 */
public enum ErrorCode {
    /**
     * face missing
     */
    FACE_MISSING,
    /**
     * action timeout
     */
    ACTION_TIMEOUT,
    /**
     * find more than one face
     */
    MULTIPLE_FACE,
    /**
     * too mush action
     */
    MUCH_MOTION,
    /**
     * this device not support liveness detection
     */
    DEVICE_NOT_SUPPORT,
    /**
     * load model error
     */
    MODEL_ERROR,
    /**
     * repeat init
     */
    ALREADY_INIT,
    /**
     * user give up detection
     */
    USER_GIVE_UP,
    /**
     * others unknown error
     */
    UNDEFINED
}
