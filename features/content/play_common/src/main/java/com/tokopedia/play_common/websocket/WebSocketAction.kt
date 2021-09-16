package com.tokopedia.play_common.websocket

/**
 * Created by jegul on 16/03/21
 */
sealed class WebSocketAction {

    data class NewMessage(val message: WebSocketResponse) : WebSocketAction()
    data class Closed(val reason: WebSocketClosedReason) : WebSocketAction()
}
