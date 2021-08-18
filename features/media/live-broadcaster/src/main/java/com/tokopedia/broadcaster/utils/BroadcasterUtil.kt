package com.tokopedia.broadcaster.utils

import com.tokopedia.broadcaster.camera.CameraManager
import com.tokopedia.broadcaster.data.BroadcasterConfig
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.VideoConfig

object BroadcasterUtil {

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
        cameraSize: Streamer.Size = config.getVideoSize()
    ) = VideoConfig().apply {
        bitRate = config.videoBitrate
        videoSize = CameraManager.verifyResolution(
            type = type,
            videoSize = Streamer.Size(cameraSize.height, cameraSize.width),
            defaultVideoSize = config.getVideoSize()
        )
        fps = config.fps
    }

}