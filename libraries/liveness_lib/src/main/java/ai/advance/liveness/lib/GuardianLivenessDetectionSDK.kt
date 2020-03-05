package ai.advance.liveness.lib

import ai.advance.common.GdCommonJar
import android.app.Application

class GuardianLivenessDetectionSDK internal constructor() {

    /**
     * device type
     */
    enum class DeviceType {
        /**
         * sample:"x86".equals(Build.CPU_ABI)
         */
        Emulator,
        /**
         * other cpu
         */
        RealPhone
    }

    companion object {

        private var mApplication: Application? = null
        internal var isSdkAuthSuccess: Boolean = false
        internal var isEmulator: Boolean = false
        /**
         * Whether the SDK handles camera permissions
         */
        var isSDKHandleCameraPermission: Boolean = false
            private set

        internal val applicationContext: Application
            get() {
                if (mApplication == null) {
                    throw NullPointerException("GuardianLivenessDetectionSDK SDK not init")
                }
                return mApplication as Application
            }

        fun initOffLine(application: Application) {
            mApplication = application
            setLogEnable(true)
            LivenessJNI.initSDK()
        }

        /**
         * set device type,different device types will call different cameras
         */
        fun setDeviceType(deviceType: DeviceType) {
            isEmulator = deviceType == DeviceType.Emulator
        }

        /**
         * let camera
         */
        fun letSDKHandleCameraPermission() {
            isSDKHandleCameraPermission = true
        }


        /**
         * set log enable status
         */
        fun setLogEnable(logEnable: Boolean) {
            GdCommonJar.initLogUtil(logEnable, BuildConfig.DEBUG, "liveness")
        }

        /**
         * Check if the current device supports live detection
         *
         * @return true means support
         */
        val isDeviceSupportLiveness: Boolean
            get() {
                val supportLiveness = LivenessJNI.nativeEnable() && CameraUtils.getTargetCameraInfo("isDeviceSupportLiveness") != null
                if (!supportLiveness) {
                    LivenessResult.setErrorCode(ErrorCode.DEVICE_NOT_SUPPORT)
                }
                return supportLiveness
            }
    }

}
