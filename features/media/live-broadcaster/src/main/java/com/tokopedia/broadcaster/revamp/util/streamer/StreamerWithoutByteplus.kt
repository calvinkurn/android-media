package com.tokopedia.broadcaster.revamp.util.streamer

import com.wmspanel.libstream.ConnectionConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerSurface

/**
 * Created By : Jonathan Darwin on March 31, 2023
 */
class StreamerWithoutByteplus(
    private val displayStreamer: Streamer?,
) : StreamerWrapper {

    override fun createConnection(connectionConfig: ConnectionConfig): Int {
        return displayStreamer?.createConnection(connectionConfig) ?: -1
    }

    override fun releaseConnection(connectionId: Int) {
        displayStreamer?.releaseConnection(connectionId)
    }

    override fun flip() {
        displayStreamer?.flip()
    }

    override fun fps(): Double? {
        return displayStreamer?.fps
    }

    override fun byteSend(connectionId: Int): Long? {
        return displayStreamer?.getBytesSent(connectionId)
    }

    override fun audioPacketsLost(connectionId: Int): Long? {
        return displayStreamer?.getAudioPacketsLost(connectionId)
    }

    override fun videoPacketsLost(connectionId: Int): Long? {
        return displayStreamer?.getVideoPacketsLost(connectionId)
    }

    override fun udpPacketsLost(connectionId: Int): Long? {
        return displayStreamer?.getUdpPacketsLost(connectionId)
    }

    override fun changeBitrate(bitrate: Int) {
        displayStreamer?.changeBitRate(bitrate)
    }

    override fun changeFpsRange(fpsRange: Streamer.FpsRange) {
        displayStreamer?.changeFpsRange(fpsRange)
    }
}
