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
     * Call this method on Activity / Fragment LifeCycle: onResume()
     */
    fun resume()

    /**
     * Call this method on Activity / Fragment LifeCycle: onPause()
     */
    fun pause()

    /**
     * Call this method on Activity / Fragment LifeCycle: onDestroy()
     */
    fun destroy()

    /**
     * add maximum live streaming duration, the default is 30minutes
     */
    fun addMaxStreamDuration(millis: Long)

    /**
     * Get Active & Finish Live Streaming State
     */
    fun getObservablePlayPusherInfoState(): LiveData<PlayPusherInfoState>

    /**
     * Get Network State when Live Streaming
     */
    fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState>
}