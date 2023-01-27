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
        const val EVENT_DELETE_MSG = 104

        const val MONITORING = 900
        const val CLOSE_CONNECTION = 999

        /**
         * This mapper only used for tracking throughout the log on the chucker,
         * By mapping the event code into readable event label name it should able to
         * easy tracks every single event occur during websocket is connected.
         */
        fun mapToEventName(event: Int): String {
            return when (event) {
                EVENT_REPLY_MESSAGE -> "REPLY_MESSAGE"
                EVENT_TOPCHAT_REPLY_MESSAGE -> "TOPCHAT_REPLY_MESSAGE"
                EVENT_TYPING -> "TYPING"
                EVENT_END_TYPING -> "END_TYPING"
                EVENT_TOPCHAT_TYPING -> "TOPCHAT_TYPING"
                EVENT_TOPCHAT_END_TYPING -> "TOPCHAT_END_TYPING"
                EVENT_TOPCHAT_READ_MESSAGE -> "TOPCHAT_READ_MESSAGE"
                EVENT_DELETE_MSG -> "DELETE_MSG"
                else -> ""
            }
        }
    }

    object Mode{
        val MODE_WEBSOCKET = 1
        val MODE_API = 2
    }
}
