package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 17/03/21.
 */
sealed class PlayLivePusherViewState {
    object Connecting: PlayLivePusherViewState()
    object Started: PlayLivePusherViewState()
    data class Resume(val isResumed: Boolean): PlayLivePusherViewState()
    object Recovered: PlayLivePusherViewState()
    object Paused: PlayLivePusherViewState()
    data class Stopped(val shouldNavigate: Boolean): PlayLivePusherViewState()
    data class Error(val errorType: PlayLivePusherErrorType, val reason: String): PlayLivePusherViewState()
}

enum class PlayLivePusherErrorType {
    NetworkPoor, NetworkLoss, ConnectFailed, SystemError
}

val PlayLivePusherErrorType.isNetworkTrouble
    get() = this == PlayLivePusherErrorType.NetworkPoor || this == PlayLivePusherErrorType.NetworkLoss