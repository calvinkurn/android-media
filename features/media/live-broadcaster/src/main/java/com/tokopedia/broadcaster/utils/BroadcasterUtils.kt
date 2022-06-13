package com.tokopedia.broadcaster.utils

import android.media.MediaCodecList
import com.tokopedia.broadcaster.camera.CameraInfo
import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.CameraConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.VideoConfig

object BroadcasterUtils {

    fun getCameraConfig(
        cameraInfo: CameraInfo,
        cameraSize: Streamer.Size
    ): CameraConfig {
        return CameraConfig().apply {
            cameraId = cameraInfo.cameraId
            videoSize = cameraSize
        }
    }

    // default config: 44.1kHz, Mono, CAMCORDER input
    fun getAudioConfig(config: BroadcasterConfig) = AudioConfig().apply {
        sampleRate = config.audioRate
        channelCount = config.audioChannelCount
        audioSource = config.audioSource
        bitRate = config.audioBitrate
    }

    // default config: h264, 2 sec. keyframe interval
    fun getVideoConfig(
        config: BroadcasterConfig,
        cameraSize: Streamer.Size = config.getVideoSize(),
        mediaCodecList: MediaCodecList = MediaCodecList(MediaCodecList.REGULAR_CODECS)
    ) = VideoConfig().apply {
        bitRate = config.videoBitrate
        videoSize = CameraManager.verifyResolution(
            type = type,
            videoSize = Streamer.Size(cameraSize.height, cameraSize.width),
            defaultVideoSize = config.getVideoSize(),
            mediaCodecList = mediaCodecList
        )
        fps = config.fps
    }

}