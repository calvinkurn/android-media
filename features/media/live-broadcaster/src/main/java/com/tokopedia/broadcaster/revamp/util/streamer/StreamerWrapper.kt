package com.tokopedia.broadcaster.revamp.util.streamer

import com.tokopedia.broadcaster.revamp.Broadcaster
import com.wmspanel.libstream.ConnectionConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerSurface

/**
 * Created By : Jonathan Darwin on March 31, 2023
 */
interface StreamerWrapper {
    
    fun createConnection(connectionConfig: ConnectionConfig): Int

    fun releaseConnection(connectionId: Int)

    fun flip()

    fun fps(): Double?

    fun byteSend(connectionId: Int): Long?

    fun audioPacketsLost(connectionId: Int): Long?

    fun videoPacketsLost(connectionId: Int): Long?

    fun udpPacketsLost(connectionId: Int): Long?

    fun changeBitrate(bitrate: Int)

    fun changeFpsRange(fpsRange: Streamer.FpsRange)
}
