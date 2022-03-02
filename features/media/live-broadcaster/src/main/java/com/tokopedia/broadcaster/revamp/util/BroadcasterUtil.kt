package com.tokopedia.broadcaster.revamp.util

import android.content.Context
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaFormat
import android.media.MediaRecorder
import android.net.ConnectivityManager
import com.tokopedia.broadcaster.revamp.util.camera.BroadcasterCamera
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.ConnectionConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.VideoConfig

/**
 * Created by meyta.taliti on 01/03/22.
 */
object BroadcasterUtil {

    fun getAudioConfig(): AudioConfig {
        return AudioConfig().apply {
            sampleRate = 44100
            channelCount = 1
            audioSource = MediaRecorder.AudioSource.CAMCORDER
            bitRate = AudioConfig.calcBitRate(sampleRate, channelCount, AudioConfig.AAC_PROFILE)
        }
    }

    fun getVideoConfig(): VideoConfig {
        return VideoConfig().apply {
            type = MediaFormat.MIMETYPE_VIDEO_AVC
            fps = 30f
            keyFrameInterval = 2
        }
    }

    fun getVideoSize(activeCamera: BroadcasterCamera): Streamer.Size {
        val recordSizes = activeCamera.recordSizes ?: return Streamer.Size(1280, 720)

        // force to 1280x720px
        var videoSize =  recordSizes.firstOrNull { it.width == 1280 && it.height == 720 }
            ?: recordSizes.first()

        // Reduce 4K to FullHD, because some encoders can fail with 4K frame size.
        // https://source.android.com/compatibility/android-cdd.html#5_2_video_encoding
        // Video resolution: 320x240px, 720x480px, 1280x720px, 1920x1080px.
        // If no FullHD support found, leave video size as is.
        if (videoSize.width > 1920 || videoSize.height > 1088) {
            for (size in recordSizes) {
                if (size.width == 1920 && (size.height == 1080 || size.height == 1088)) {
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
        return Streamer.Size(1280, 720)
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