package com.tokopedia.play.broadcaster.view.state

import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayLiveViewState {
    object Connecting: PlayLiveViewState()
    object Started: PlayLiveViewState()
    data class Resume(val isResumed: Boolean): PlayLiveViewState()
    object Recovered: PlayLiveViewState()
    object Paused: PlayLiveViewState()
    data class Stopped(val shouldNavigate: Boolean): PlayLiveViewState()
    data class Error(val error: PlayLivePusherException): PlayLiveViewState()
}

val PlayLiveViewState.isStarted: Boolean
    get() = this == PlayLiveViewState.Started

val PlayLiveViewState.isRecovered: Boolean
    get() = this == PlayLiveViewState.Recovered