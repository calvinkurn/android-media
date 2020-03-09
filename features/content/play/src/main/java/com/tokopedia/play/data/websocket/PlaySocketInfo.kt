package com.tokopedia.play.data.websocket


/**
 * Created by mzennis on 2020-03-02.
 */
sealed class PlaySocketInfo {
    object Reconnect : PlaySocketInfo()
    data class Error(val throwable: Throwable) : PlaySocketInfo()
}