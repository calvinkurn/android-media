package ai.advance.liveness.lib;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;

import ai.advance.common.utils.LogUtil;

/**
 * createTime:2019-11-07
 *
 * @author fan.zhang@advance.ai
 */
class CameraUtils {
    /**
     * get camera info
     *
     * @return null when camera is disable
     */
    static Camera.CameraInfo getTargetCameraInfo(String fromWay) {
        int cameraId = GuardianLivenessDetectionSDK.isEmulator ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        try {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            return info;
        } catch (Exception e) {
            LogUtil.sdkLogE(fromWay + ",cameraId:[" + cameraId + "] getCameraInfo happened error:" + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Obtain camera rotation Angle
     */
    static int getCameraAngle(Activity activity) {
        int rotateAngle;
        Camera.CameraInfo info = getTargetCameraInfo("getCameraAngle");
        if (info == null) {
            return -1;
        } else {
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotateAngle = (info.orientation + degrees) % 360;
                // compensate the mirror
                rotateAngle = (360 - rotateAngle) % 360;
            } else { // back-facing
                rotateAngle = (info.orientation - degrees + 360) % 360;
            }
            return rotateAngle;
        }
    }
}
