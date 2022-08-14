package com.tokopedia.broadcaster.revamp.util

import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaFormat
import android.media.MediaRecorder
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
    private const val HIGHEST_VIDEO_WIDTH = 1920
    private const val HIGHEST_VIDEO_HEIGHT = 1080

    /**
     * 1280x720 should be supported by every device running Android 4.1+
     * https://source.android.com/compatibility/4.1/android-4.1-cdd.pdf [chapter 5.2]
     */
    private val defaultResolution = Streamer.Size(
        DEFAULT_RESOLUTION_VIDEO_WIDTH,
        DEFAULT_RESOLUTION_VIDEO_HEIGHT
    )

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

    fun getVideoSize(supportedPreviewSizes: List<Streamer.Size>?, targetAspectRatio: Double): Streamer.Size {
        val recordSizes = findSizeWithPreferredAspectRatio(targetAspectRatio, supportedPreviewSizes)
        if (!recordSizes.isNullOrEmpty()) return findBestResolution(recordSizes)

        // Nothing found, use default
        return supportedPreviewSizes?.firstOrNull() ?: defaultResolution
    }

    /*
    Set the same video size for both cameras
    If not possible (for example front camera has no FullHD support)
    try to find video size with the same aspect ratio
    */
    fun findFlipSize(supportedPreviewSizes: List<Streamer.Size>?, videoSize: Streamer.Size): Streamer.Size {
        val recordSizes = findSizeWithPreferredAspectRatio(videoSize.ratio, supportedPreviewSizes)
        if (!recordSizes.isNullOrEmpty()) return findBestResolution(recordSizes)

        // Same aspect ratio not found, search for less or similar frame sides
        val flipSizeSimilarFrameSize = supportedPreviewSizes?.firstOrNull {
            it.height <= videoSize.height && it.width <= videoSize.width
        }
        if (flipSizeSimilarFrameSize != null) return flipSizeSimilarFrameSize

        // Nothing found, use default
        return supportedPreviewSizes?.firstOrNull() ?: defaultResolution
    }

    @Suppress("MagicNumber")
    private fun findSizeWithPreferredAspectRatio(
        targetAspectRatio: Double,
        recordSizes: List<Streamer.Size>?
    ): List<Streamer.Size>? {

        fun isSameAspectRatio(size: Streamer.Size): Boolean {
            val aspectDiff = targetAspectRatio / size.ratio - 1
            return abs(aspectDiff) < 0.01
        }

        return recordSizes?.filter {
            // Reduce 4K to FullHD, because some encoders can fail with 4K frame size.
            (it.width <= HIGHEST_VIDEO_WIDTH || it.height <= HIGHEST_VIDEO_HEIGHT)
                    && isSameAspectRatio(it)
        }
    }

    @Suppress("MagicNumber")
    private fun findBestResolution(recordSizes: List<Streamer.Size>): Streamer.Size {
        var bestResolution = recordSizes.first()
        var lastSize = 0
        for (size in recordSizes) {
            val currentSize = size.width + size.height
            if (currentSize > lastSize) {
                bestResolution = size
            }
            lastSize = currentSize
        }
        return bestResolution
    }

    fun verifyResolution(type: String?, videoSize: Streamer.Size): Streamer.Size {
        val info = selectCodec(type)
        val capabilities = info?.getCapabilitiesForType(type)
        val videoCapabilities = capabilities?.videoCapabilities
        if (videoCapabilities?.isSizeSupported(videoSize.width, videoSize.height) == true) {
            return videoSize
        }
        return defaultResolution
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
}