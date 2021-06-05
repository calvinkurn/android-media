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
    object Pause: PlayLivePusherState()
    object Stop: PlayLivePusherState()
    data class Error(val reason: String): PlayLivePusherState()
}

val PlayLivePusherState.isPushing: Boolean
    get() = this == PlayLivePusherState.Started
            || this == PlayLivePusherState.Resumed
            || this == PlayLivePusherState.Recovered

val PlayLivePusherState.isPaused: Boolean
    get() = this == PlayLivePusherState.Pause

val PlayLivePusherState.isStopped: Boolean
    get() = this == PlayLivePusherState.Stop

val PlayLivePusherState.isError: Boolean
get() = this is PlayLivePusherState.Error
