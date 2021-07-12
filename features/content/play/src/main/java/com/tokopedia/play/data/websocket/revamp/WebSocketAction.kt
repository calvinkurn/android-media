package com.tokopedia.play.data.websocket.revamp

import com.tokopedia.websocket.WebSocketResponse

/**
 * Created by jegul on 16/03/21
 */
sealed class WebSocketAction {

    data class NewMessage(val message: WebSocketResponse) : WebSocketAction()
    data class Closed(val reason: WebSocketClosedReason) : WebSocketAction()
}
