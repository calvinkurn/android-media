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
    suspend fun create()

    /**
     * Open camera for preview live streaming
     */
    suspend fun startPreview(surfaceView: SurfaceView)

    /**
     * Stop previewing
     */
    fun stopPreview()

    /**
     * Start live streaming by providing ingest url
     */
    suspend fun startPush(ingestUrl: String)

    /**
     * Restart live streaming
     */
    suspend fun restartPush()

    /**
     * Stop live streaming
     */
    suspend fun stopPush()

    /**
     * Switch camera between front and back, the default is front
     */
    suspend fun switchCamera()

    /**
     * Resume pusher
     */
    suspend fun resume()

    /**
     * Pause pusher
     */
    suspend fun pause()

    /**
     * Destroy pusher
     */
    suspend fun destroy()

    /**
     * add maximum live streaming duration
     */
    fun addMaxStreamDuration(millis: Long)

    /**
     * restart live streaming duration
     */
    fun restartStreamDuration(millis: Long)

    /**
     * add maximum pause duration when streaming
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