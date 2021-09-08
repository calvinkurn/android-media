package com.tokopedia.broadcaster

import com.tokopedia.broadcaster.data.BroadcasterConnection
import com.tokopedia.kotlin.extensions.view.orZero
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.VideoConfig

interface LibStreamerGL {
    val fps: Double
    val activeCameraId: String

    fun stopAudioCapture()
    fun stopVideoCapture()
    fun flip()
    fun release()
    fun startVideoCapture()
    fun startAudioCapture()
    fun createConnection(mConnection: BroadcasterConnection): Int?
    fun releaseConnection(id: Int)
    fun changeAudioConfig(config: AudioConfig)
    fun changeVideoConfig(config: VideoConfig)
    fun changeBitRate(bitrate: Int)
    fun changeFpsRange(newRange: Streamer.FpsRange?)
    fun setFrontMirror(isPreview: Boolean, isStream: Boolean)
    fun getBytesSent(connectionId: Int): Long
    fun getAudioPacketsLost(connectionId: Int): Long
    fun getVideoPacketsLost(connectionId: Int): Long
    fun getUdpPacketsLost(connectionId: Int): Long
}

class LibStreamerGLFactory constructor(
    private var streamerGL: StreamerGL? = null
) : LibStreamerGL {

    override val fps get() = streamerGL?.fps?: 0.0

    override val activeCameraId get() = streamerGL?.activeCameraId?: ""

    override fun stopAudioCapture() {
        streamerGL?.stopAudioCapture()
    }

    override fun stopVideoCapture() {
        streamerGL?.stopVideoCapture()
    }

    override fun flip() {
        streamerGL?.flip()
    }

    override fun release() {
        streamerGL?.release()
    }

    override fun startVideoCapture() {
        streamerGL?.startVideoCapture()
    }

    override fun startAudioCapture() {
        streamerGL?.startAudioCapture()
    }

    override fun createConnection(mConnection: BroadcasterConnection): Int? {
        return streamerGL?.createConnection(mConnection)
    }

    override fun releaseConnection(id: Int) {
        streamerGL?.releaseConnection(id)
    }

    override fun changeAudioConfig(config: AudioConfig) {
        streamerGL?.changeAudioConfig(config)
    }

    override fun changeVideoConfig(config: VideoConfig) {
        streamerGL?.changeVideoConfig(config)
    }

    override fun changeBitRate(bitrate: Int) {
        streamerGL?.changeBitRate(bitrate)
    }

    override fun changeFpsRange(newRange: Streamer.FpsRange?) {
        streamerGL?.changeFpsRange(newRange)
    }

    override fun setFrontMirror(isPreview: Boolean, isStream: Boolean) {
        streamerGL?.setFrontMirror(isPreview, isStream)
    }

    override fun getBytesSent(connectionId: Int): Long {
        return streamerGL?.getBytesSent(connectionId).orZero()
    }

    override fun getAudioPacketsLost(connectionId: Int): Long {
        return streamerGL?.getAudioPacketsLost(connectionId)?: 0L
    }

    override fun getVideoPacketsLost(connectionId: Int): Long {
        return streamerGL?.getVideoPacketsLost(connectionId)?: 0L
    }

    override fun getUdpPacketsLost(connectionId: Int): Long {
        return streamerGL?.getUdpPacketsLost(connectionId)?: 0L
    }

}