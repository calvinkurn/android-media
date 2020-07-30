package com.tokopedia.play.broadcaster.pusher

import android.view.SurfaceView
import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoListener
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
     * Call this method to stop an ongoing preview. However, you cannot call this method to stop a preview during live stream.
     */
    fun stopPreview()

    /**
     * Start live streaming by providing ingest url
     */
    suspend fun startPush(ingestUrl: String)

    /**
     * Call this method to restart live stream. or after you receive any callback event notification related to an error.
     */
    suspend fun restartPush()

    /**
     *  Call this method to stop ongoing live stream.
     */
    suspend fun stopPush()

    /**
     * Switch camera between front and back, the default is front
     */
    suspend fun switchCamera()

    /**
     * Call this method to enable disabled live stream. Then, the SDK resumes the audio and video preview and live stream.
     * After the streaming is resumed, the audio and video preview and streaming are back to normal.
     */
    suspend fun resume()

    /**
     * Call this method to disable ongoing live stream. Then, the SDK pauses the video preview and video stream at the last frame.
     */
    suspend fun pause()

    /**
     * Destroy pusher
     */
    fun destroy()

    /**
     * add live streaming duration
     */
    fun addStreamDuration(durationInMillis: Long)

    /**
     * add maximum live streaming duration
     */
    fun addMaxStreamDuration(durationInMillis: Long)

    /**
     * restart live streaming duration
     */
    fun restartStreamDuration(durationInMillis: Long)

    /**
     * add maximum pause duration when streaming
     */
    fun addMaxPauseDuration(durationInMillis: Long)

    /**
     * Callback pusher info during live streaming
     */
    fun addPusherInfoListener(playPusherInfoListener: PlayPusherInfoListener)

    /**
     * Get Network State during Live Streaming
     */
    fun getObservablePlayPusherNetworkState(): LiveData<PlayPusherNetworkState>

    fun isPushing(): Boolean
}