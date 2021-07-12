package com.tokopedia.broadcaster.utils

import com.wmspanel.libstream.Streamer

data class BroadcasterConfig(
    val videoWidth: Int = 1280,
    val videoHeight: Int = 720,
    val fps: Float = 30.0f,
    val videoBitrate: Int = 2000 * 1000, // Kbps -> bps
) {

    fun getVideoSize() = Streamer.Size(videoHeight, videoWidth)
}