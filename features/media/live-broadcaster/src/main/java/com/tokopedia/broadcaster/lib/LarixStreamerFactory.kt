package com.tokopedia.broadcaster.lib

import android.view.Surface
import com.tokopedia.broadcaster.utils.safeExecute
import com.tokopedia.kotlin.extensions.view.orZero
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.StreamerGL
import com.wmspanel.libstream.VideoConfig

class LarixStreamerFactory constructor(
    private var streamerGL: StreamerGL? = null
) : LarixStreamer {

    override val fps
        get() = streamerGL?.safeExecute { fps } ?: 0.0

    override val activeCameraId
        get() = streamerGL?.safeExecute { activeCameraId } ?: ""

    override fun setSurface(surface: Surface) {
        streamerGL?.safeExecute { setSurface(surface) }
    }

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

    override fun createConnection(mConnection: BroadcasterConnection)
        = streamerGL?.safeExecute { createConnection(mConnection) }

    override fun getBytesSent(connectionId: Int)
        = streamerGL?.safeExecute { getBytesSent(connectionId) }.orZero()

    override fun getAudioPacketsLost(connectionId: Int)
        = streamerGL?.safeExecute { getAudioPacketsLost(connectionId) }.orZero()

    override fun getVideoPacketsLost(connectionId: Int)
        = streamerGL?.safeExecute { getVideoPacketsLost(connectionId) }.orZero()

    override fun getUdpPacketsLost(connectionId: Int)
        = streamerGL?.safeExecute { getUdpPacketsLost(connectionId) }.orZero()

}