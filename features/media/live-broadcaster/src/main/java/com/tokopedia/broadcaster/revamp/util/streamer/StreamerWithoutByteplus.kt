package com.tokopedia.broadcaster.revamp.util.streamer

import com.wmspanel.libstream.ConnectionConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.StreamerSurface

/**
 * Created By : Jonathan Darwin on March 31, 2023
 */
class StreamerWithoutByteplus : StreamerWrapper {

    override var displayStreamer: StreamerGL? = null

    override var pusherStreamer: StreamerSurface? = null

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

    override fun activeCameraId(): String? {
        return displayStreamer?.activeCameraId
    }

    override fun activeCameraVideoSize(): Streamer.Size? {
        return displayStreamer?.activeCameraVideoSize
    }

    override fun setSurfaceSize(surfaceSize: Streamer.Size) {
        displayStreamer?.setSurfaceSize(surfaceSize)
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

    override fun stopVideoCapture() {
        displayStreamer?.stopVideoCapture()
        pusherStreamer?.stopVideoCapture()
    }

    override fun stopAudioCapture() {
        displayStreamer?.stopAudioCapture()
        pusherStreamer?.stopAudioCapture()
    }

    override fun release() {
        displayStreamer?.release()
        pusherStreamer?.release()

        displayStreamer = null
        pusherStreamer = null
    }
}
