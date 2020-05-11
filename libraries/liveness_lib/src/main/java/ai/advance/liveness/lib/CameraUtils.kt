package ai.advance.liveness.lib

import ai.advance.common.utils.LogUtil
import android.app.Activity
import android.hardware.Camera
import android.view.Surface

internal object CameraUtils {
    /**
     * get camera info
     *
     * @return null when camera is disable
     */
    fun getTargetCameraInfo(fromWay: String): Camera.CameraInfo? {
        val cameraId = if (GuardianLivenessDetectionSDK.isEmulator) Camera.CameraInfo.CAMERA_FACING_BACK else Camera.CameraInfo.CAMERA_FACING_FRONT
        try {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(cameraId, info)
            return info
        } catch (e: Exception) {
            LogUtil.sdkLogE(fromWay + ",cameraId:[" + cameraId + "] getCameraInfo happened error:" + e.localizedMessage)
        }

        return null
    }

    /**
     * Obtain camera rotation Angle
     */
    fun getCameraAngle(activity: Activity): Int {
        var rotateAngle: Int
        val info = getTargetCameraInfo("getCameraAngle")
        if (info == null) {
            return -1
        } else {
            val rotation = activity.windowManager.defaultDisplay.rotation
            var degrees = 0
            when (rotation) {
                Surface.ROTATION_0 -> degrees = 0
                Surface.ROTATION_90 -> degrees = 90
                Surface.ROTATION_180 -> degrees = 180
                Surface.ROTATION_270 -> degrees = 270
            }
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotateAngle = (info.orientation + degrees) % 360
                // compensate the mirror
                rotateAngle = (360 - rotateAngle) % 360
            } else { // back-facing
                rotateAngle = (info.orientation - degrees + 360) % 360
            }
            return rotateAngle
        }
    }
}
