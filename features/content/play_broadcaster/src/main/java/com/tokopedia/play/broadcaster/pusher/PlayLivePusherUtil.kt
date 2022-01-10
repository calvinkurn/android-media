package com.tokopedia.play.broadcaster.pusher

import com.tokopedia.play.broadcaster.pusher.camera.CameraManager
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.VideoConfig


/**
 * Created by mzennis on 04/08/21.
 */
object PlayLivePusherUtil {

    // default config: 44.1kHz, Mono, CAMCORDER input
    fun getAudioConfig(config: PlayLivePusherConfig) = AudioConfig().apply {
        sampleRate = config.audioRate
        channelCount = config.audioChannelCount
        audioSource = config.audioSource
        bitRate = config.audioBitrate
    }

    // default config: h264, 2 sec. keyframe interval
    fun getVideoConfig(
        config: PlayLivePusherConfig,
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