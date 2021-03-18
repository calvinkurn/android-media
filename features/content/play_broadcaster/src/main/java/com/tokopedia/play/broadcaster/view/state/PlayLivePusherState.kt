package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayLivePusherState {
    object Connecting: PlayLivePusherState()
    object Started: PlayLivePusherState()
    object Resumed: PlayLivePusherState()
    object Paused: PlayLivePusherState()
    object Stopped: PlayLivePusherState()
    data class Error(val errorState: PlayLivePusherErrorState, val throwable: Throwable): PlayLivePusherState()
}

sealed class PlayLivePusherErrorState {
    object NetworkPoor : PlayLivePusherErrorState()
    object NetworkLoss : PlayLivePusherErrorState()
    data class ConnectFailed(val onRetry: () -> Unit = {}) : PlayLivePusherErrorState()
    object SystemError : PlayLivePusherErrorState()
}