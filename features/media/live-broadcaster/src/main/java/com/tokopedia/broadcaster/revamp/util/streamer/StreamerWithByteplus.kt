package com.tokopedia.broadcaster.revamp.util.streamer

import com.wmspanel.libstream.ConnectionConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.StreamerSurface

/**
 * Created By : Jonathan Darwin on March 31, 2023
 */
class StreamerWithByteplus : StreamerWrapper {

    override var displayStreamer: StreamerGL? = null

    override var pusherStreamer: StreamerSurface? = null

    override fun createConnection(connectionConfig: ConnectionConfig): Int {
        return pusherStreamer?.createConnection(connectionConfig) ?: -1
    }

    override fun releaseConnection(connectionId: Int) {
        pusherStreamer?.releaseConnection(connectionId)
    }

    override fun flip() {
        displayStreamer?.flip()
    }

    override fun fps(): Double? {
        return pusherStreamer?.fps
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
