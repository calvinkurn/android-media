package com.tokopedia.chat_common.network

/**
 * @author : Steven 29/11/18
 */

class ChatUrl {

    companion object {

        var TOPCHAT = "https://chat.tokopedia.com/";
        var CHAT_WEBSOCKET_DOMAIN = "wss://chat.tokopedia.com"

        const val CONNECT_WEBSOCKET = "/connect"
        const val REPLY = "/tc/v1/reply"
        const val DELETE = "/tc/v1/delete"
        const val GET_TEMPLATE = "tc/v1/templates"
    }
}
