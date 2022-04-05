package com.tokopedia.play.broadcaster.pusher.revamp.state

/**
 * Created by meyta.taliti on 05/04/22.
 */
sealed interface PlayBroadcasterState {
    object Started: PlayBroadcasterState
    data class Resume(val startedBefore: Boolean, val shouldContinue: Boolean): PlayBroadcasterState
    object Paused: PlayBroadcasterState
    data class Error(val cause: Throwable): PlayBroadcasterState
    object Stopped: PlayBroadcasterState
}