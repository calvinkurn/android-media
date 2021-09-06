package com.tokopedia.play_common.websocket

/**
 * Created by jegul on 16/03/21
 */
sealed class WebSocketClosedReason {

    object Intended : WebSocketClosedReason()
    data class Error(val error: Throwable) : WebSocketClosedReason()
}