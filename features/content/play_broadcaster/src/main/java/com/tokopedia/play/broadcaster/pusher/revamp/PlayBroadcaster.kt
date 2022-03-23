package com.tokopedia.play.broadcaster.pusher.revamp

import android.content.Context
import android.os.Handler
import android.view.SurfaceHolder
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.state.BroadcastState
import com.tokopedia.play.broadcaster.di.ActivityRetainedScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Created by meyta.taliti on 03/03/22.
 */
class PlayBroadcaster(
    activityContext: Context,
    handler: Handler?,
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

    private var isStarted = false

    init {
        broadcaster.init(activityContext, handler)
    }

    override fun start(rtmpUrl: String) {
        broadcaster.start(rtmpUrl)
        isStarted = true
    }

    override fun create(holder: SurfaceHolder, surfaceSize: Broadcaster.Size) {
        broadcaster.create(holder, surfaceSize)
        updateAspectFrameSize()
    }

    override fun flip() {
        broadcaster.flip()
        updateAspectFrameSize()
    }

    override fun stop() {
        isStarted = false
        broadcaster.release()
    }

    override fun destroy() {
        broadcaster.destroy()
    }

    /**
     * only return false when user manually click stop
     */
    fun isStartedBefore() = isStarted

    fun getBroadcastState(): Flow<BroadcastState> = callbackFlow {
        val listener = object : Broadcaster.Listener {
            override fun onBroadcastStateChanged(state: BroadcastState) {
                trySend(state)
            }
        }

        addListener(listener)
        awaitClose { removeListener(listener) }
    }

    fun getBroadcastInitState(): Flow<BroadcastInitState> = callbackFlow {
        val listener = object : Broadcaster.Listener {
            override fun onBroadcastInitStateChanged(state: BroadcastInitState) {
                trySend(state)
            }
        }

        addListener(listener)
        awaitClose { removeListener(listener) }
    }

    private fun updateAspectFrameSize() {
        val size = broadcaster.activeCameraVideoSize ?: return
        callback.updateAspectRatio(size.height.toDouble() / size.width.toDouble())
    }

    interface Callback {
        fun updateAspectRatio(aspectRatio: Double)
    }
}