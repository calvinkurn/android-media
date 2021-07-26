package com.tokopedia.chat_common.data

/**
 * @author by nisie on 12/12/18.
 */
class WebsocketEvent{

    object Event {
        const val EVENT_REPLY_MESSAGE = 102
        const val EVENT_TOPCHAT_REPLY_MESSAGE = 103
        const val EVENT_TYPING = 201
        const val EVENT_END_TYPING = 202
        const val EVENT_TOPCHAT_TYPING = 203
        const val EVENT_TOPCHAT_END_TYPING = 204
        const val EVENT_TOPCHAT_READ_MESSAGE = 301

        const val MONITORING = 900
        const val CLOSE_CONNECTION = 999
    }

    object Mode{
        val MODE_WEBSOCKET = 1
        val MODE_API = 2
    }
}