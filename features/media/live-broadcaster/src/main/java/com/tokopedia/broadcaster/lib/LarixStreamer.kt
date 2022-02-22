package com.tokopedia.broadcaster.lib

import android.view.Surface
import com.wmspanel.libstream.AudioConfig
import com.wmspanel.libstream.Streamer
import com.wmspanel.libstream.VideoConfig

interface LarixStreamer {
    val fps: Double
    val activeCameraId: String

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