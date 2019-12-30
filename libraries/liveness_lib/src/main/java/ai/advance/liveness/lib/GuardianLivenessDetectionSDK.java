package ai.advance.liveness.lib;

import android.app.Application;

import ai.advance.common.GdCommonJar;

public class GuardianLivenessDetectionSDK {

    private static Application mApplication;
    static boolean isAuthSuccess;
    static boolean isEmulator;
    private static boolean sdkHandleCameraPermission;

    GuardianLivenessDetectionSDK() {
    }

    static boolean isSdkAuthSuccess() {
        return isAuthSuccess;
    }

    static Application getApplicationContext() {
        if (mApplication == null) {
            throw new NullPointerException("GuardianLivenessDetectionSDK SDK not init");
        }
        return mApplication;
    }

    public static void initOffLine(Application application) {
        mApplication = application;
        setLogEnable(true);
        LivenessJNI.initSDK();
    }

    /**
     * device type
     */
    public enum DeviceType {
        /**
         * sample:"x86".equals(Build.CPU_ABI)
         */
        Emulator,
        /**
         * other cpu
         */
        RealPhone
    }

    /**
     * set device type,different device types will call different cameras
     */
    public static void setDeviceType(DeviceType deviceType) {
        GuardianLivenessDetectionSDK.isEmulator = deviceType == DeviceType.Emulator;
    }

    /**
     * let camera
     */
    public static void letSDKHandleCameraPermission() {
        GuardianLivenessDetectionSDK.sdkHandleCameraPermission = true;
    }

    /**
     * Whether the SDK handles camera permissions
     */
    public static boolean isSDKHandleCameraPermission() {
        return GuardianLivenessDetectionSDK.sdkHandleCameraPermission;
    }


    /**
     * set log enable status
     */
    public static void setLogEnable(boolean logEnable) {
        GdCommonJar.initLogUtil(logEnable, BuildConfig.DEBUG, "liveness");
    }

    /**
     * Check if the current device supports live detection
     *
     * @return true means support
     */
    public static boolean isDeviceSupportLiveness() {
        boolean supportLiveness = LivenessJNI.nativeEnable() && CameraUtils.getTargetCameraInfo("isDeviceSupportLiveness") != null;
        if (!supportLiveness) {
            LivenessResult.setErrorCode(ErrorCode.DEVICE_NOT_SUPPORT);
        }
        return supportLiveness;
    }

}
