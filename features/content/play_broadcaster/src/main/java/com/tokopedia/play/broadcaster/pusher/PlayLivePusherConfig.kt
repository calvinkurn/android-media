package com.tokopedia.play.broadcaster.pusher

import com.wmspanel.libstream.Streamer


/**
 * Created by mzennis on 03/06/21.
 */
data class PlayLivePusherConfig(
    val videoWidth: Int = 1280,
    val videoHeight: Int = 720,
    val fps: Float = 30.0f,
    val videoBitrate: Int = 2000 * 1000, // Kbps -> bps
) {

    fun getVideoSize() = Streamer.Size(videoHeight, videoWidth)
}