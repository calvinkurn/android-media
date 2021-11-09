package com.tokopedia.play.broadcaster.pusher


/**
 * Created by mzennis on 04/06/21.
 */
sealed class PlayLivePusherState {
    object Idle: PlayLivePusherState()
    object Connecting: PlayLivePusherState()
    object Started: PlayLivePusherState()
    object Resumed: PlayLivePusherState()
    object Recovered: PlayLivePusherState()
    object Paused: PlayLivePusherState()
    object Stopped: PlayLivePusherState()
    data class Error(val reason: String): PlayLivePusherState()
}

val PlayLivePusherState.isPushing: Boolean
    get() = this == PlayLivePusherState.Started
            || this == PlayLivePusherState.Resumed
            || this == PlayLivePusherState.Recovered

val PlayLivePusherState.isResumed: Boolean
    get() = this == PlayLivePusherState.Resumed

val PlayLivePusherState.isPaused: Boolean
    get() = this == PlayLivePusherState.Paused

val PlayLivePusherState.isStopped: Boolean
    get() = this == PlayLivePusherState.Stopped

val PlayLivePusherState.isError: Boolean
get() = this is PlayLivePusherState.Error
