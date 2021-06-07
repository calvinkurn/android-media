package com.tokopedia.play.broadcaster.pusher


/**
 * Created by mzennis on 03/06/21.
 */
data class PlayLivePusherConfig(
    val fps: Int = 30,
    val videoBitrate: Int = 900 * 1024,
    val videoOrientation: Int = 90,
    val audioBitrate: Int = 64 * 1024,
    val audioSampleRate: Int = 32000,
    val audioStereo: Boolean = true,
    val audioEchoCanceler: Boolean = false,
    val audioNoiseSuppressor: Boolean = false,
    val maxRetry: Int = 15,
    val reconnectDelay: Long = 3000
)