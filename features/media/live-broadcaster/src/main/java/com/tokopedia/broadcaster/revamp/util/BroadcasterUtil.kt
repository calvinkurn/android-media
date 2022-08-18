package com.tokopedia.broadcaster.revamp.util

import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaFormat
import android.media.MediaRecorder
import com.tokopedia.broadcaster.revamp.util.camera.BroadcasterCamera
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.ConnectionConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.VideoConfig
import kotlin.math.abs

/**
 * Created by meyta.taliti on 01/03/22.
 */
object BroadcasterUtil {

    private const val DEFAULT_RESOLUTION_VIDEO_WIDTH = 1280
    private const val DEFAULT_RESOLUTION_VIDEO_HEIGHT = 720
    private const val HIGH_RESOLUTION_VIDEO_WIDTH = 1920
    private const val HIGH_RESOLUTION_VIDEO_HEIGHT = 1080

    fun getAudioConfig(): AudioConfig {
        return AudioConfig().apply {
            audioSource = MediaRecorder.AudioSource.CAMCORDER
            bitRate = AudioConfig.calcBitRate(sampleRate, channelCount, AudioConfig.AAC_PROFILE)
        }
    }

    fun getVideoConfig(): VideoConfig {
        return VideoConfig().apply {
            type = MediaFormat.MIMETYPE_VIDEO_AVC
        }
    }

    fun getVideoSize(activeCamera: BroadcasterCamera): Streamer.Size {
        val recordSizes = activeCamera.recordSizes ?: return Streamer.Size(DEFAULT_RESOLUTION_VIDEO_WIDTH, DEFAULT_RESOLUTION_VIDEO_HEIGHT)

        var videoSize = recordSizes.first()

        // Reduce 4K to FullHD, because some encoders can fail with 4K frame size.
        // https://source.android.com/compatibility/android-cdd.html#5_2_video_encoding
        // Video resolution: 320x240px, 720x480px, 1280x720px, 1920x1080px.
        // If no FullHD support found, leave video size as is.
        if (videoSize.width > HIGH_RESOLUTION_VIDEO_WIDTH || videoSize.height > HIGH_RESOLUTION_VIDEO_HEIGHT) {
            for (size in recordSizes) {
                if (size.width == HIGH_RESOLUTION_VIDEO_WIDTH && size.height == HIGH_RESOLUTION_VIDEO_HEIGHT) {
                    videoSize = size
                    break
                }
            }
        }
        return videoSize
    }

    fun verifyResolution(type: String?, videoSize: Streamer.Size): Streamer.Size {
        val info = selectCodec(type)
        val capabilities = info?.getCapabilitiesForType(type)
        val videoCapabilities = capabilities?.videoCapabilities
        if (videoCapabilities?.isSizeSupported(videoSize.width, videoSize.height) == true) {
            return videoSize
        }
        // 1280x720 should be supported by every device running Android 4.1+
        // https://source.android.com/compatibility/4.1/android-4.1-cdd.pdf [chapter 5.2]
        return Streamer.Size(DEFAULT_RESOLUTION_VIDEO_WIDTH, DEFAULT_RESOLUTION_VIDEO_HEIGHT)
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

    fun getConnectionConfig(url: String) = ConnectionConfig().apply {
        uri = url
        mode = Streamer.MODE.AUDIO_VIDEO
        auth = Streamer.AUTH.DEFAULT
    }

    // Set the same video size for both cameras
    // If not possible (for example front camera has no FullHD support)
    // try to find video size with the same aspect ratio
    @Suppress("MagicNumber")
    fun findFlipSize(cameraInfo: BroadcasterCamera, videoSize: Streamer.Size): Streamer.Size? {
        val flipSize = cameraInfo.recordSizes?.firstOrNull { it == videoSize }

        // If secondary camera supports same resolution, use it
        if (flipSize != null) return flipSize

        // If same resolution not found, search for same aspect ratio
        fun findFlipSizeForSameAspectRatio(
            target: Double,
            recordSize: Streamer.Size
        ): Boolean {
            return if (recordSize.width < videoSize.width) {
                val aspectRatio = recordSize.width / recordSize.height
                val aspectDiff = target / aspectRatio - 1
                abs(aspectDiff) < 0.01
            } else false
        }

        val targetAspectRatio = videoSize.width.toDouble() / videoSize.height
        val flipSizeSameAspectRatio = cameraInfo.recordSizes?.firstOrNull {
            findFlipSizeForSameAspectRatio(targetAspectRatio, it)
        }
        if (flipSizeSameAspectRatio != null) return flipSizeSameAspectRatio

        // Same aspect ratio not found, search for less or similar frame sides
        val flipSizeSimilarFrameSize = cameraInfo.recordSizes?.firstOrNull {
            it.height <= videoSize.height && it.width <= videoSize.width
        }
        if (flipSizeSimilarFrameSize != null) return flipSizeSimilarFrameSize

        // Nothing found, use default
        return cameraInfo.recordSizes?.first()
    }
}