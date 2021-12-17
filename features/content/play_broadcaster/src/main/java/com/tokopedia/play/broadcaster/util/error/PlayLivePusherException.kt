package com.tokopedia.play.broadcaster.util.error


/**
 * Created by mzennis on 18/06/21.
 */
open class PlayLivePusherException(val reason: String) : Throwable(reason) {

    val type: PlayLivePusherErrorType
        get() = mType

    private val mType: PlayLivePusherErrorType

    init {
        mType = parse(reason)
    }

    fun parse(reason: String): PlayLivePusherErrorType {
        return when {
            reason.contains("connect fail:", true) -> PlayLivePusherErrorType.ConnectFailed
            reason.contains("network:", true) -> PlayLivePusherErrorType.NetworkLoss
            reason.contains("system:", true) -> PlayLivePusherErrorType.SystemError
            else -> PlayLivePusherErrorType.Unknown
        }
    }

}

enum class PlayLivePusherErrorType {
    NetworkPoor, NetworkLoss, ConnectFailed, SystemError, Unknown
}

val PlayLivePusherErrorType.isNetworkTrouble
    get() = this == PlayLivePusherErrorType.NetworkPoor || this == PlayLivePusherErrorType.NetworkLoss