package com.tokopedia.play.broadcaster.fake

import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimerState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created by Jonathan Darwin on 18 March 2024
 */
class FakeBroadcastTimer(
    override val remainingDuration: Long = 10000,
    override val isPastPauseDuration: Boolean = false,
) : PlayBroadcastTimer {

    private var mDuration: Long = 0L
    private var mListener: PlayBroadcastTimer.Listener? = null

    val duration: Long
        get() = mDuration
    override val stateChanged: Flow<PlayBroadcastTimerState>
        get() = callbackFlow {
            val listener = object : PlayBroadcastTimer.Listener {
                override fun onStateChanged(state: PlayBroadcastTimerState) {
                    trySend(state)
                }
            }

            setListener(listener)
            awaitClose { setListener(null) }
        }

    override fun setupDuration(duration: Long, maxDuration: Long) {
        
    }

    override fun setupPauseDuration(duration: Long) {
        
    }

    override fun setListener(listener: PlayBroadcastTimer.Listener?) {
        mListener = listener
    }

    override fun start() {
        
    }

    override fun stop() {
        
    }

    override fun restart(duration: Long) {
        
    }

    override fun pause() {
        
    }

    override fun destroy() {
        
    }

    fun triggerTimerTick() {
        mDuration += 1000
        mListener?.onStateChanged(PlayBroadcastTimerState.Active(mDuration))
    }

    fun triggerTimerFinish() {
        mListener?.onStateChanged(PlayBroadcastTimerState.Finish)
    }
}
