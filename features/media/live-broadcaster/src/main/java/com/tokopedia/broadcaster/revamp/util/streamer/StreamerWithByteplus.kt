package com.tokopedia.broadcaster.revamp.util.streamer

import com.wmspanel.libstream.ConnectionConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerSurface

/**
 * Created By : Jonathan Darwin on March 31, 2023
 */
class StreamerWithByteplus(
    private val displayStreamer: Streamer?,
    private val pusherStreamer: StreamerSurface?,
) : StreamerWrapper {

    override fun createConnection(connectionConfig: ConnectionConfig): Int {
        return pusherStreamer?.createConnection(connectionConfig) ?: -1
    }

    override fun releaseConnection(connectionId: Int) {
        pusherStreamer?.releaseConnection(connectionId)
    }

    override fun flip() {
        displayStreamer?.flip()
        pusherStreamer?.flip()
    }

    override fun fps(): Double? {
        return pusherStreamer?.fps
    }

    override fun byteSend(connectionId: Int): Long? {
        return pusherStreamer?.getBytesSent(connectionId)
    }

    override fun audioPacketsLost(connectionId: Int): Long? {
        return pusherStreamer?.getAudioPacketsLost(connectionId)
    }

    override fun videoPacketsLost(connectionId: Int): Long? {
        return pusherStreamer?.getVideoPacketsLost(connectionId)
    }

    override fun udpPacketsLost(connectionId: Int): Long? {
        return pusherStreamer?.getUdpPacketsLost(connectionId)
    }

    override fun changeBitrate(bitrate: Int) {
        displayStreamer?.changeBitRate(bitrate)
        pusherStreamer?.changeBitRate(bitrate)
    }

    override fun changeFpsRange(fpsRange: Streamer.FpsRange) {
        displayStreamer?.changeFpsRange(fpsRange)
        pusherStreamer?.changeFpsRange(fpsRange)
    }
}
