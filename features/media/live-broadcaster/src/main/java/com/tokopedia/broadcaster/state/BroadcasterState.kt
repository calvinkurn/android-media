package com.tokopedia.broadcaster.state

sealed class BroadcasterState {
    object Idle: BroadcasterState()
    object Connecting: BroadcasterState()
    object Started: BroadcasterState()
    object Resumed: BroadcasterState()
    object Recovered: BroadcasterState()
    object Pause: BroadcasterState()
    object Stop: BroadcasterState()

    data class Error(
        val reason: String
    ): BroadcasterState()
}

val BroadcasterState.isPushing: Boolean
    get() = this == BroadcasterState.Started
            || this == BroadcasterState.Resumed
            || this == BroadcasterState.Recovered

val BroadcasterState.isPaused: Boolean
    get() = this == BroadcasterState.Pause

val BroadcasterState.isStopped: Boolean
    get() = this == BroadcasterState.Stop

val BroadcasterState.isError: Boolean
    get() = this is BroadcasterState.Error