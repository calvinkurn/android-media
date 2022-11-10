package com.tokopedia.play.broadcaster.pusher.timer

import kotlinx.coroutines.flow.Flow

/**
 * Created by meyta.taliti on 18/03/22.
 */
interface PlayBroadcastTimer {

    val remainingDuration: Long

    val isPastPauseDuration: Boolean

    val stateChanged: Flow<PlayBroadcastTimerState>

    fun setupDuration(duration: Long, maxDuration: Long)

    fun setupPauseDuration(duration: Long)

    fun setListener(listener: Listener?)

    fun start()

    fun stop()

    fun restart(duration: Long)

    fun pause()

    fun destroy()

    interface Listener {
        fun onStateChanged(state: PlayBroadcastTimerState)
    }
}