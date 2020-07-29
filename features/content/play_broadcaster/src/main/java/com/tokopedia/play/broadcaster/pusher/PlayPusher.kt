package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView
import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState


/**
 * Created by mzennis on 24/05/20.
 */
interface PlayPusher {

    /**
     * Initialize SDK
     */
    fun create()

    /**
     * Open camera for preview live streaming
     */
    fun startPreview(surfaceView: SurfaceView)

    /**
     * Stop previewing
     */
    fun stopPreview()

    /**
     * Start live streaming by providing ingest url
     */
    fun startPush(ingestUrl: String)

    /**
     * Restart live streaming
     */
    fun restartPush()

    /**
     * Stop live streaming
     */
    fun stopPush()

    /**
     * Switch camera between front and back, the default is front
     */
    fun switchCamera()

    /**
     * Resume pusher
     */
    fun resume()

    /**
     * Pause pusher
     */
    fun pause()

    /**
     * Destroy pusher
     */
    fun destroy()

    /**
     * add maximum live streaming duration, the default is 30minutes
     */
    fun addMaxStreamDuration(millis: Long)

    /**
     * add maximum pause duration when streaming, the default is 1minutes
     */
    fun addMaxPauseDuration(millis: Long)

    /**
     * Get Live Streaming State
     */
    fun getObservablePlayPusherInfoState(): LiveData<PlayPusherInfoState>

    /**
     * Get Network State during Live Streaming
     */
    fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState>

    fun isPushing(): Boolean
}