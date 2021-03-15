package com.tokopedia.play.broadcaster.pusher.apsara


/**
 * Created by mzennis on 02/07/20.
 */
sealed class ApsaraLivePusherState {
    object Idle : ApsaraLivePusherState()
    object Live : ApsaraLivePusherState()
    object Pause : ApsaraLivePusherState()
    object Stop : ApsaraLivePusherState()
    object Restart : ApsaraLivePusherState()
    data class Error(val errorStatus: ApsaraLivePusherErrorStatus) : ApsaraLivePusherState()
}

enum class ApsaraLivePusherErrorStatus { NetworkPoor, NetworkLoss, ConnectFailed, ReconnectFailed, SystemError }