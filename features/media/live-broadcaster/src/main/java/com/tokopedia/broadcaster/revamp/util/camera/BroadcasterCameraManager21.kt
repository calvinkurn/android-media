package com.tokopedia.broadcaster.revamp.util.camera

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaCodec
import android.os.Build
import com.wmspanel.libstream.Streamer

/**
 * Created by meyta.taliti on 01/03/22.
 */
class BroadcasterCameraManager21(private val context: Context) : BroadcasterCameraManager {

    override fun getCameraList(): List<BroadcasterCamera> {
        return try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            return cameraManager.cameraIdList
                .mapNotNull { getCamera(it) }
                .distinctBy { it.lensFacing }
        } catch (e: CameraAccessException) {
            emptyList()
        }
    }

    override fun getCamera(cameraId: String): BroadcasterCamera? {
        return try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

            val fpsRanges =
                characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)
            val cameraFpsRanges = fpsRanges?.map { Streamer.FpsRange(it.lower, it.upper) }

            val recordSizes = map?.getOutputSizes(MediaCodec::class.java)
            val cameraRecordSizes = recordSizes?.map { Streamer.Size(it.width, it.height) }

            val lensFacing = when {
                characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK -> {
                    BroadcasterCamera.LENS_FACING_BACK
                }
                characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT -> {
                    BroadcasterCamera.LENS_FACING_FRONT
                }
                else -> BroadcasterCamera.LENS_FACING_EXTERNAL
            }

            val physicalCameras = mutableListOf<BroadcasterCamera>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val cameraCapability = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                if (cameraCapability != null) {
                    for (capability in cameraCapability) {
                        if (capability == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA) {
                            // Prior to API level 29, all returned IDs are guaranteed to be returned by
                            // CameraManager#getCameraIdList, and can be opened directly by CameraManager#openCamera.
                            for (physicalCameraId in characteristics.physicalCameraIds) {
                                val physicalCamera = getCamera(physicalCameraId) ?: continue
                                physicalCameras.add(physicalCamera)
                            }
                            break
                        }
                    }
                }
            }

            BroadcasterCamera(
                cameraId,
                lensFacing,
                cameraRecordSizes,
                cameraFpsRanges,
                physicalCameras
            )

        } catch (e: Exception) {
            null
        }
    }
}