package com.tokopedia.broadcaster

import android.view.Surface
import com.tokopedia.broadcaster.data.BroadcasterConnection
import com.tokopedia.broadcaster.utils.safeExecute
import com.tokopedia.kotlin.extensions.view.orZero
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.VideoConfig

interface LibStreamerGL {
    val fps: Double
    val activeCameraId: String
    val portrait: Int

    fun setSurface(surface: Surface)
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

    override val fps
        get() = streamerGL?.safeExecute { fps } ?: 0.0

    override val activeCameraId
        get() = streamerGL?.safeExecute { activeCameraId } ?: ""

    override val portrait: Int
        get() = StreamerGL.ORIENTATIONS.PORTRAIT

    override fun stopAudioCapture() {
        streamerGL?.safeExecute { stopAudioCapture() }
    }

    override fun stopVideoCapture() {
        streamerGL?.safeExecute { stopVideoCapture() }
    }

    override fun flip() {
        streamerGL?.safeExecute { flip() }
    }

    override fun release() {
        streamerGL?.safeExecute { release() }
    }

    override fun startVideoCapture() {
        streamerGL?.safeExecute { startVideoCapture() }
    }

    override fun startAudioCapture() {
        streamerGL?.safeExecute { startAudioCapture() }
    }

    override fun createConnection(mConnection: BroadcasterConnection): Int? {
        return streamerGL?.safeExecute { createConnection(mConnection) }
    }

    override fun releaseConnection(id: Int) {
        streamerGL?.safeExecute { releaseConnection(id) }
    }

    override fun changeAudioConfig(config: AudioConfig) {
        streamerGL?.safeExecute { changeAudioConfig(config) }
    }

    override fun changeVideoConfig(config: VideoConfig) {
        streamerGL?.safeExecute { changeVideoConfig(config) }
    }

    override fun changeBitRate(bitrate: Int) {
        streamerGL?.safeExecute { changeBitRate(bitrate) }
    }

    override fun changeFpsRange(newRange: Streamer.FpsRange?) {
        streamerGL?.safeExecute { changeFpsRange(newRange) }
    }

    override fun setFrontMirror(isPreview: Boolean, isStream: Boolean) {
        streamerGL?.safeExecute { setFrontMirror(isPreview, isStream) }
    }

    override fun getBytesSent(connectionId: Int): Long {
        return streamerGL?.safeExecute { getBytesSent(connectionId) }.orZero()
    }

    override fun getAudioPacketsLost(connectionId: Int): Long {
        return streamerGL?.safeExecute { getAudioPacketsLost(connectionId) } ?: 0L
    }

    override fun getVideoPacketsLost(connectionId: Int): Long {
        return streamerGL?.safeExecute { getVideoPacketsLost(connectionId) } ?: 0L
    }

    override fun getUdpPacketsLost(connectionId: Int): Long {
        return streamerGL?.safeExecute { getUdpPacketsLost(connectionId) } ?: 0L
    }

    override fun setSurface(surface: Surface) {
        streamerGL?.safeExecute { setSurface(surface) }
    }

}