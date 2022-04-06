package com.tokopedia.play.broadcaster.pusher.revamp

import android.content.Context
import android.os.Handler
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.state.BroadcastState
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import com.tokopedia.play.broadcaster.pusher.revamp.state.PlayBroadcasterState
import javax.inject.Inject

/**
 * Created by meyta.taliti on 03/03/22.
 */
class PlayBroadcaster(
    activityContext: Context,
    private val handler: Handler?,
    private val broadcaster: Broadcaster,
    private val callback: Callback,
) : Broadcaster by broadcaster {

    @ActivityRetainedScope
    class Factory @Inject constructor(
        private val broadcaster: Broadcaster,
    ) {
        fun create(
            activityContext: Context,
            handler: Handler?,
            callback: Callback
        ): PlayBroadcaster {
            return PlayBroadcaster(activityContext, handler, broadcaster, callback)
        }
    }

    private var mLastIngestUrl = ""
    private var isStartedBefore = false

    private val broadcastListener = object : Broadcaster.Listener {
        override fun onBroadcastInitStateChanged(state: BroadcastInitState) {
            callback.onBroadcastInitStateChanged(state)
        }

        override fun onBroadcastStateChanged(state: BroadcastState) {
            val newState = when(state) {
                is BroadcastState.Error -> PlayBroadcasterState.Error(state.cause)
                BroadcastState.Started -> PlayBroadcasterState.Started
                BroadcastState.Recovered -> PlayBroadcasterState.Recovered
                else -> null
            }
            if (newState != null) callback.onBroadcastStateChanged(newState)
        }
    }

    inner class RetryRunnable : Runnable {
        override fun run() {
            broadcaster.retry()
        }
    }

    init {
        broadcaster.init(activityContext, handler)
        broadcaster.addListener(broadcastListener)
    }

    /**
     * start with cached rtmpUrl
     */
    fun start() {
        if (mLastIngestUrl.isBlank()) return
        start(mLastIngestUrl)
    }

    override fun start(rtmpUrl: String) {
        mLastIngestUrl = rtmpUrl
        broadcaster.start(rtmpUrl)
        isStartedBefore = true
    }

    override fun create(holder: SurfaceHolder, surfaceSize: Broadcaster.Size) {
        broadcaster.create(holder, surfaceSize)
        updateAspectFrameSize()
    }

    override fun flip() {
        broadcaster.flip()
        updateAspectFrameSize()
    }

    fun doRetry() {
        handler?.postDelayed(RetryRunnable(), RETRY_TIMEOUT)
    }

    override fun stop() {
        isStartedBefore = false
        broadcaster.release()
        callback.onBroadcastStateChanged(PlayBroadcasterState.Stopped)
    }

    override fun destroy() {
        broadcaster.removeListener(broadcastListener)
        broadcaster.destroy()
    }

    fun resume(shouldContinue: Boolean) {
        callback.onBroadcastStateChanged(
            PlayBroadcasterState.Resume(
                startedBefore = isStartedBefore,
                shouldContinue = shouldContinue
            )
        )
    }

    fun pause() {
        callback.onBroadcastStateChanged(PlayBroadcasterState.Paused)
    }

    private fun updateAspectFrameSize() {
        val size = broadcaster.activeCameraVideoSize ?: return
        callback.updateAspectRatio(size.height.toDouble() / size.width.toDouble())
    }

    interface Callback {
        fun updateAspectRatio(aspectRatio: Double)
        fun onBroadcastInitStateChanged(state: BroadcastInitState)
        fun onBroadcastStateChanged(state: PlayBroadcasterState)
    }

    companion object {
        private const val RETRY_TIMEOUT = 3000L
    }
}