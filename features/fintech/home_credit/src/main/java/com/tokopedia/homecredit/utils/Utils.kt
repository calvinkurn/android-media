package com.tokopedia.homecredit.utils

import android.hardware.Camera
import android.hardware.Camera.CameraInfo

object Utils {
    val isFrontCameraAvailable: Boolean
        get() {
            val numCameras = Camera.getNumberOfCameras()
            for (i in 0 until numCameras) {
                val info = CameraInfo()
                Camera.getCameraInfo(i, info)
                if (CameraInfo.CAMERA_FACING_FRONT == info.facing) {
                    return true
                }
            }
            return false
        }
}