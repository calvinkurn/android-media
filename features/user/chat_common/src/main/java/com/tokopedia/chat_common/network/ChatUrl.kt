package com.tokopedia.chat_common.network

import com.tokopedia.network.constant.TkpdBaseURL

/**
 * @author : Steven 29/11/18
 */

var CHATBOT_BASE_URL = "https://api.tokopedia.com/";
var TOPCHAT = "https://chat.tokopedia.com/";
var TOPCHAT_BASE_URL = TkpdBaseURL.JS_DOMAIN;


class ChatUrl {

    companion object {
        const val CHAT_WEBSOCKET = "/connect"
        const val GET_MESSAGE = "/tc/v1/list_message"
        const val GET_REPLY = "/tc/v2/list_reply/{msgId}"
        const val GET_USER_CONTACT = "/tc/v1/message_contact/"
        const val REPLY = "/tc/v1/reply"
        const val LISTEN_WEBSOCKET = "/connect"
        const val SEARCH = "/tc/v1/search"
        const val DELETE = "/tc/v1/delete"
        const val SEND_MESSAGE = "/tc/v1/send"
        const val GET_TOPCHAT_NOTIFICATION = "tc/v1/notif_unreads"
        const val GET_TEMPLATE = "tc/v1/templates"
        const val GET_TEMPLATE_OLD = "tc/v1/chat_templates"
        const val UPDATE_TEMPLATE = "/tc/v1/templates/{index}"
        const val DELETE_TEMPLATE = "/tc/v1/templates/{index}"
        const val SET_TEMPLATE = "tc/v1/templates"
        const val SET_TEMPLATE_OLD = "tc/v1/update_chat_templates"
        const val CREATE_TEMPLATE = "tc/v1/templates"
        const val GET_EXISTING_CHAT = "/tc/v1/existing_chat"
    }
}
