package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 22/09/20.
 */
sealed class LivePusherState {
    object Connecting : LivePusherState()
    object Started : LivePusherState()
    object Recovered : LivePusherState()
    object Paused : LivePusherState()
    data class Stopped(val shouldNavigate: Boolean) : LivePusherState()
    data class Error(val errorStatus: LivePusherErrorStatus) : LivePusherState()
}

sealed class LivePusherErrorStatus {
    object NetworkPoor : LivePusherErrorStatus()
    object NetworkLoss : LivePusherErrorStatus()
    data class ConnectFailed(val onRetry: () -> Unit) : LivePusherErrorStatus()
    data class UnRecoverable(val onRetry: () -> Unit) : LivePusherErrorStatus()
}