package com.tokopedia.broadcaster.revamp.util.camera

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.os.Build

/**
 * Created by meyta.taliti on 01/03/22.
 */
interface BroadcasterCameraManager {

    fun getCameraList(): List<BroadcasterCamera>

    fun getCamera(cameraId: String): BroadcasterCamera?

    companion object {

        fun newInstance(context: Context): BroadcasterCameraManager {
            return if (allowCamera2Support(context)) {
                BroadcasterCameraManager21(context)
            } else {
                BroadcasterCameraManager16()
            }
        }

        fun allowCamera2Support(context: Context): Boolean {
            // Some known camera api dependencies and issues:
            // Moto X Pure Edition, Android 6.0; Screen freeze reported with Camera2
            if (Build.MANUFACTURER.equals("motorola", ignoreCase = true) && Build.MODEL.equals(
                    "clark_retus",
                    ignoreCase = true
                )
            ) {
                return false
            }

            /*
             LEGACY Camera2 implementation has problem with aspect ratio.
             Rather than allowing Camera2 API on all Android 5+ devices, we restrict it to
             cases where all cameras have at least LIMITED support.
             (E.g., Nexus 6 has FULL support on back camera, LIMITED support on front camera.)
             For now, devices with only LEGACY support should still use Camera API.
            */
            val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                for (cameraId in manager.cameraIdList) {
                    val characteristics = manager.getCameraCharacteristics(cameraId)
                    val support = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N
                        && support == CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY
                    ) {
                        return false
                    }
                }
            } catch (e: CameraAccessException) {
                return false
            } catch (e: IllegalArgumentException) {
                return false
            }
            return true
        }
    }
}