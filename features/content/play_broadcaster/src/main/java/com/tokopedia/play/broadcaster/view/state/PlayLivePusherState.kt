package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayLivePusherState {
    object Connecting: PlayLivePusherState()
    object Start: PlayLivePusherState()
    data class Resume(val isResumed: Boolean): PlayLivePusherState()
    object Pause: PlayLivePusherState()
    object Recovered: PlayLivePusherState()
    data class Stop(val isStopped: Boolean, val shouldNavigate: Boolean): PlayLivePusherState()
    data class Error(val errorState: PlayLivePusherErrorState, val throwable: Throwable): PlayLivePusherState()
}

sealed class PlayLivePusherErrorState {
    object NetworkPoor : PlayLivePusherErrorState()
    object NetworkLoss : PlayLivePusherErrorState()
    data class ConnectFailed(val onRetry: () -> Unit = {}) : PlayLivePusherErrorState()
    object SystemError : PlayLivePusherErrorState()
}