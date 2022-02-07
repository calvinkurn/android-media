package com.tokopedia.chat_common.network

import com.tokopedia.url.TokopediaUrl

/**
 * @author : Steven 29/11/18
 */

class ChatUrl {

    companion object {

        var TOPCHAT = TokopediaUrl.getInstance().CHAT
        var CHAT_WEBSOCKET_DOMAIN = TokopediaUrl.getInstance().WS_CHAT

        const val CONNECT_WEBSOCKET = "/connect"
        const val REPLY = "/tc/v1/reply"
        const val DELETE = "/tc/v1/delete"
        const val GET_TEMPLATE = "tc/v1/templates"
    }
}
