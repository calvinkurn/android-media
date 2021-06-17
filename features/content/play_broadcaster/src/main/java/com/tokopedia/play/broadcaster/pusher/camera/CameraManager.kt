package com.tokopedia.play.broadcaster.pusher.camera

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.os.Build
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.Streamer.FpsRange
import kotlin.math.atan


/**
 * Created by mzennis on 17/06/21.
 */
object CameraManager {

    fun getAvailableCameras(context: Context): List<CameraInfo> {
        val cameraList: MutableList<CameraInfo> = mutableListOf()
        try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraIdList = cameraManager.cameraIdList
            for (cameraId in cameraIdList) {
                val camera = getCameraInfo(context, cameraId) ?: continue
                if (camera.lensFacing == CameraType.External || camera.lensFacing == CameraType.Unknown) continue
                cameraList.add(camera)
            }
        } catch (ignored: CameraAccessException) { }
        return cameraList
    }

    private fun getCameraInfo(context: Context, cameraId: String): CameraInfo? {
        val cameraInfo = CameraInfo(cameraId)
        try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val fpsRanges = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)!!
            cameraInfo.fpsRanges = arrayOfNulls(fpsRanges.size)
            for (i in fpsRanges.indices) {
                cameraInfo.fpsRanges[i] = FpsRange(fpsRanges[i].lower, fpsRanges[i].upper)
            }
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val recordSizes = map!!.getOutputSizes(MediaCodec::class.java)
            cameraInfo.recordSizes = arrayOfNulls(recordSizes.size)
            for (j in recordSizes.indices) {
                cameraInfo.recordSizes[j] = Streamer.Size(recordSizes[j].width, recordSizes[j].height)
            }
            cameraInfo.lensFacing = when(characteristics.get(CameraCharacteristics.LENS_FACING)) {
                CameraCharacteristics.LENS_FACING_BACK -> CameraType.Back
                CameraCharacteristics.LENS_FACING_FRONT -> CameraType.Front
                else -> CameraType.External
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                for (capability in characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)!!) {
                    if (capability == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA) {
                        // Prior to API level 29, all returned IDs are guaranteed to be returned by
                        // CameraManager#getCameraIdList, and can be opened directly by CameraManager#openCamera.
                        for (physicalCameraId in characteristics.physicalCameraIds) {
                            val physicalCamera = getCameraInfo(context, physicalCameraId) ?: continue
                            cameraInfo.physicalCameras.add(physicalCamera)
                        }
                        break
                    }
                }
            }
            var angrad = 1.1
            val sensorSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE)
            val focalLengths = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)
            if (sensorSize != null && focalLengths != null && focalLengths.isNotEmpty()) {
                angrad = 2.0f * atan((sensorSize.width / (2.0f * focalLengths[0])).toDouble())
            }
            cameraInfo.fov = Math.toDegrees(angrad).toFloat()
            cameraInfo.maxZoom = characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM).orZero()
            cameraInfo.isZoomSupported = cameraInfo.maxZoom > 1.0f
            cameraInfo.isTorchSupported = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE).orFalse()
        } catch (e: NullPointerException) {
            return null
        } catch (e: CameraAccessException) {
            return null
        }
        return cameraInfo
    }

    fun getVideoSize(cameraInfo: CameraInfo): Streamer.Size? {
        if (cameraInfo.recordSizes.isEmpty()) {
            return null
        }

        var videoSize = cameraInfo.recordSizes.first()

        // Reduce 4K to FullHD, because some encoders can fail with 4K frame size.
        // https://source.android.com/compatibility/android-cdd.html#5_2_video_encoding
        // Video resolution: 320x240px, 720x480px, 1280x720px, 1920x1080px.
        // If no FullHD support found, leave video size as is.
        if (videoSize!!.width > 1920 || videoSize.height > 1088) {
            var newIndex = 0
            for (size in cameraInfo.recordSizes) {
                if (size == null) continue
                if (size.width == 1920 && (size.height == 1080 || size.height == 1088)) {
                    videoSize = size
                    break
                }
                newIndex++
            }
        }
        return videoSize
    }

    fun verifyResolution(type: String?, videoSize: Streamer.Size): Streamer.Size {
        val info = selectCodec(type)
        val capabilities = info?.getCapabilitiesForType(type)
        val videoCapabilities = capabilities?.videoCapabilities
        if (videoCapabilities?.isSizeSupported(videoSize.width, videoSize.height) == true) {
            return Streamer.Size(720, 1280)
        }
        return videoSize
    }

    private fun selectCodec(mimeType: String?): MediaCodecInfo? {
        val mediaCodecList = MediaCodecList(MediaCodecList.REGULAR_CODECS)
        for (codecInfo in mediaCodecList.codecInfos) {
            if (!codecInfo.isEncoder) {
                continue
            }
            for (type in codecInfo.supportedTypes) {
                if (type.equals(mimeType, ignoreCase = true)) {
                    return codecInfo
                }
            }
        }
        return null
    }
}