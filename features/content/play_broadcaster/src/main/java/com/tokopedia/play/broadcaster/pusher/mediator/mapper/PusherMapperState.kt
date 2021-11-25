package com.tokopedia.play.broadcaster.pusher.mediator.mapper

import com.tokopedia.broadcaster.state.BroadcasterState
import com.tokopedia.play.broadcaster.pusher.*

fun PlayLivePusherState.map(): BroadcasterState {
    return when(this) {
        PlayLivePusherState.Idle -> BroadcasterState.Idle
        PlayLivePusherState.Connecting -> BroadcasterState.Connecting
        PlayLivePusherState.Started -> BroadcasterState.Started
        PlayLivePusherState.Resumed -> BroadcasterState.Resumed
        PlayLivePusherState.Recovered -> BroadcasterState.Recovered
        PlayLivePusherState.Paused -> BroadcasterState.Paused
        PlayLivePusherState.Stopped -> BroadcasterState.Stopped
        is PlayLivePusherState.Error -> BroadcasterState.Error(reason)
    }
}

fun BroadcasterState.map(): PlayLivePusherState {
    return when(this) {
        BroadcasterState.Idle -> PlayLivePusherState.Idle
        BroadcasterState.Connecting -> PlayLivePusherState.Connecting
        BroadcasterState.Started -> PlayLivePusherState.Started
        BroadcasterState.Resumed -> PlayLivePusherState.Resumed
        BroadcasterState.Recovered -> PlayLivePusherState.Recovered
        BroadcasterState.Paused -> PlayLivePusherState.Paused
        BroadcasterState.Stopped -> PlayLivePusherState.Stopped
        is BroadcasterState.Error -> PlayLivePusherState.Error(reason)
    }
}