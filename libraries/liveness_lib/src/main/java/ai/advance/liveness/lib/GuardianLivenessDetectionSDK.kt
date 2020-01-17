package ai.advance.liveness.lib

import ai.advance.common.GdCommonJar
import android.app.Application

class GuardianLivenessDetectionSDK {

    enum class DeviceType {
        Emulator,
        RealPhone
    }

    companion object {

        private var mApplication: Application? = null
        var isSdkAuthSuccess: Boolean = false
        var isEmulator: Boolean = false
        var isSDKHandleCameraPermission: Boolean = false
            private set

        val applicationContext: Application
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

        fun setDeviceType(deviceType: DeviceType) {
            isEmulator = deviceType == DeviceType.Emulator
        }

        fun letSDKHandleCameraPermission() {
            isSDKHandleCameraPermission = true
        }

        private fun setLogEnable(logEnable: Boolean) {
            GdCommonJar.initLogUtil(logEnable, BuildConfig.DEBUG, "liveness")
        }

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
