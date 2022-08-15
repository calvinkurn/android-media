package com.tokopedia.broadcaster.revamp.util.camera

import android.hardware.Camera
import com.wmspanel.libstream.Streamer

/**
 * Created by meyta.taliti on 01/03/22.
 */
class BroadcasterCameraManager16 : BroadcasterCameraManager {

    override fun getCameraList(): List<BroadcasterCamera> {
        val cameraList = mutableListOf<BroadcasterCamera>()
        for (i in 0 until Camera.getNumberOfCameras()) {
            val camera = getCamera(i.toString()) ?: continue
            cameraList.add(camera)
        }
        return cameraList
    }

    override fun getCamera(cameraId: String): BroadcasterCamera? {

        var camera: Camera? = null

        val camId = try {
            Integer.parseInt(cameraId)
        } catch (e: NumberFormatException) {
            0
        }

        return try {
            camera = Camera.open(camId)
            val param = camera.parameters

            val info = Camera.CameraInfo()
            Camera.getCameraInfo(camId, info)

            val cameraRecordSizes = param.supportedPreviewSizes?.map {
                Streamer.Size(it.width, it.height)
            }

            val cameraFpsRanges = param.supportedPreviewFpsRange?.map {
                Streamer.FpsRange(it[0], it[1])
            }

            val lensFacing = if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                BroadcasterCamera.LENS_FACING_BACK
            } else {
                BroadcasterCamera.LENS_FACING_FRONT
            }

            BroadcasterCamera(
                cameraId,
                lensFacing,
                cameraRecordSizes,
                cameraFpsRanges,
                null
            )
        }  catch (e: Exception) {
            null
        } finally {
            camera?.release()
        }
    }
}