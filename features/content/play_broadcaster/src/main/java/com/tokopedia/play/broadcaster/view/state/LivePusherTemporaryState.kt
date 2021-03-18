package com.tokopedia.play.broadcaster.view.state


/**
 * Created by mzennis on 22/09/20.
 */
sealed class LivePusherTemporaryState {
    object Connecting : LivePusherTemporaryState()
    object Started : LivePusherTemporaryState()
    object Recovered : LivePusherTemporaryState()
    object Paused : LivePusherTemporaryState()
    data class Stopped(val shouldNavigate: Boolean) : LivePusherTemporaryState()
    data class Error(val errorStatus: LivePusherErrorStatus) : LivePusherTemporaryState()
}

sealed class LivePusherErrorStatus {
    object NetworkPoor : LivePusherErrorStatus()
    object NetworkLoss : LivePusherErrorStatus()
    data class ConnectFailed(val onRetry: () -> Unit) : LivePusherErrorStatus()
    data class UnRecoverable(val onRetry: () -> Unit) : LivePusherErrorStatus()
}