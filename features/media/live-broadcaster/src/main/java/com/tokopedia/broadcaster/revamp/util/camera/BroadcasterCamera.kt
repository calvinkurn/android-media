package com.tokopedia.broadcaster.revamp.util.camera

import com.wmspanel.libstream.Streamer
import java.lang.StringBuilder

/**
 * Created by meyta.taliti on 01/03/22.
 */
data class BroadcasterCamera(
    val cameraId: String,
    val lensFacing: Int,
    val recordSizes: List<Streamer.Size>?,
    val fpsRanges: List<Streamer.FpsRange>?,
    val physicalCameras: List<BroadcasterCamera>?,
) {

    override fun toString(): String {
        val sb = StringBuilder(1024)
            .append("cameraId=").append(cameraId)

        when (lensFacing) {
            LENS_FACING_FRONT -> sb.append("(FRONT)")
            LENS_FACING_BACK -> sb.append("(BACK)")
            LENS_FACING_EXTERNAL -> sb.append("(EXTERNAL)")
            else -> { }
        }

        if (!physicalCameras.isNullOrEmpty()) {
            sb.append(", isMultiCamera=").append(physicalCameras.isNotEmpty()).append(";")
        }

        if (recordSizes != null) {
            sb.append("\nrecordSizes=")
            for (size in recordSizes) {
                sb.append(size).append(";")
            }
        }

        if (fpsRanges != null) {
            sb.append("\nfpsRanges=")
            for (range in fpsRanges) {
                sb.append(range).append(";")
            }
        }

        return sb.toString();
    }

    data class CamId(
        val id: String,
        val physicalId: String,
    ) {

        override fun toString(): String {
            return String.format("id=%s, physicalId=%s", id, physicalId)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || this.javaClass != other.javaClass) {
                return false
            }
            val (id1, physicalId1) = other as CamId
            return (id == id1
                    && physicalId == physicalId1)
        }
    }

    companion object {
        const val LENS_FACING_FRONT = 0
        const val LENS_FACING_BACK = 1
        const val LENS_FACING_EXTERNAL = 2
    }
}