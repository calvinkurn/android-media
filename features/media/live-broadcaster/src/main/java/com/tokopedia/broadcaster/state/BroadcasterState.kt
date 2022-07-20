package com.tokopedia.broadcaster.state

sealed class BroadcasterState {
    object Idle: BroadcasterState()
    object Connecting: BroadcasterState()
    object Started: BroadcasterState()
    object Resumed: BroadcasterState()
    object Recovered: BroadcasterState()
    object Paused: BroadcasterState()
    object Stopped: BroadcasterState()

    data class Error(
        val reason: String = ""
    ): BroadcasterState()
}

val BroadcasterState.isPushing: Boolean
    get() = this == BroadcasterState.Started
            || this == BroadcasterState.Resumed
            || this == BroadcasterState.Recovered

val BroadcasterState.isResumed: Boolean
    get() = this == BroadcasterState.Resumed

val BroadcasterState.isPaused: Boolean
    get() = this == BroadcasterState.Paused

val BroadcasterState.isStopped: Boolean
    get() = this == BroadcasterState.Stopped

val BroadcasterState.isError: Boolean
    get() = this is BroadcasterState.Error