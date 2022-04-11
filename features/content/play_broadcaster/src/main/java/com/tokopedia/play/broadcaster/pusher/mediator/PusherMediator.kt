package com.tokopedia.play.broadcaster.pusher.mediator

import android.content.Context
import android.os.Handler
import com.tokopedia.broadcaster.mediator.LivePusherConfig
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediatorListener
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherState

interface PusherMediator {
    val remainingDurationInMillis: Long
    val config: LivePusherConfig
    val ingestUrl: String

    fun getLivePusherState(): PlayLivePusherState

    fun init(context: Context, handler: Handler)
    fun switchCamera()
    fun start(url: String)
    fun resume()
    fun pause()
    fun stop()
    fun release()
    fun setLiveStreamingDuration(durationInMillis: Long, maxDuration: Long)
    fun setLiveStreamingPauseDuration(durationInMillis: Long)
    fun startLiveStreaming(ingestUrl: String, withTimer: Boolean)
    fun stopLiveStreaming()
    fun startLiveTimer()
    fun restartLiveTimer(duration: Long, maxDuration: Long)
    fun destroy()
    fun addListener(listener: PlayLivePusherMediatorListener)
    fun removeListener(listener: PlayLivePusherMediatorListener)
    fun clearListener()
    fun onCameraChanged(surfaceView: SurfaceAspectRatioView)
    fun onCameraDestroyed()
}