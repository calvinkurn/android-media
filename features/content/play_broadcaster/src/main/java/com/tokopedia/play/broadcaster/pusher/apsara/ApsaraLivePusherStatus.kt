package com.tokopedia.play.broadcaster.pusher.apsara


/**
 * Created by mzennis on 02/07/20.
 */
sealed class ApsaraLivePusherStatus {
    object Idle : ApsaraLivePusherStatus()
    data class Live(val activeStatus: ApsaraLivePusherActiveStatus) : ApsaraLivePusherStatus()
    object Pause : ApsaraLivePusherStatus()
    object Stop : ApsaraLivePusherStatus()
    object Restart : ApsaraLivePusherStatus()
    data class Error(val errorStatus: ApsaraLivePusherErrorStatus) : ApsaraLivePusherStatus()
}

enum class ApsaraLivePusherErrorStatus { NetworkPoor, NetworkLoss, ConnectFailed, ReconnectFailed, SystemError }

enum class ApsaraLivePusherActiveStatus { Normal, Recover }