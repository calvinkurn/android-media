package com.tokopedia.broadcaster.camera

import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.Streamer.FpsRange

class CameraInfo(val cameraId: String) {

    var recordSizes: Array<Streamer.Size?> = emptyArray()
    var lensFacing: CameraType = CameraType.Unknown
    var fpsRanges: Array<FpsRange?> = emptyArray()
    var physicalCameras: ArrayList<CameraInfo> = ArrayList()
    var fov: Float = 0f
    var isZoomSupported: Boolean = false
    var maxZoom: Float = 1.0f
    var isTorchSupported: Boolean = false

    override fun toString(): String {
        val stringBuilder = StringBuilder(1024).append("cameraId=").append(cameraId)
        when (lensFacing) {
            CameraType.Front -> stringBuilder.append("(FRONT)")
            CameraType.Back -> stringBuilder.append("(BACK)")
            CameraType.External -> stringBuilder.append("(EXTERNAL)")
            CameraType.Unknown -> stringBuilder.append("(UNKNOWN)")
        }
        stringBuilder.append(", isMultiCamera=").append(physicalCameras.size > 0).append(";")
        stringBuilder.append("\nrecordSizes=")
        for (size in recordSizes) {
            stringBuilder.append(size).append(";")
        }
        stringBuilder.append("\nfpsRanges=")
        for (range in fpsRanges) {
            stringBuilder.append(range).append(";")
        }
        stringBuilder.append("\nfov=").append(fov).append(";")
        stringBuilder.append("\nisZoomSupported=").append(isZoomSupported)
            .append(", maxZoom=").append(maxZoom).append(";")
        return stringBuilder.toString()
    }
}