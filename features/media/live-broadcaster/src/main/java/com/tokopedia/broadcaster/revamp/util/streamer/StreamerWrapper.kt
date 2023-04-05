package com.tokopedia.broadcaster.revamp.util.streamer

import com.wmspanel.libstream.ConnectionConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.StreamerSurface

/**
 * Created By : Jonathan Darwin on March 31, 2023
 */
interface StreamerWrapper {

    var displayStreamer: StreamerGL?

    var pusherStreamer: StreamerSurface?
    
    fun createConnection(connectionConfig: ConnectionConfig): Int

    fun releaseConnection(connectionId: Int)

    fun flip()

    fun fps(): Double?

    fun activeCameraId(): String?

    fun activeCameraVideoSize(): Streamer.Size?

    fun setSurfaceSize(surfaceSize: Streamer.Size)

    fun byteSend(connectionId: Int): Long?

    fun audioPacketsLost(connectionId: Int): Long?

    fun videoPacketsLost(connectionId: Int): Long?

    fun udpPacketsLost(connectionId: Int): Long?

    fun changeBitrate(bitrate: Int)

    fun changeFpsRange(fpsRange: Streamer.FpsRange)

    fun stopVideoCapture()

    fun stopAudioCapture()

    fun release()
}
