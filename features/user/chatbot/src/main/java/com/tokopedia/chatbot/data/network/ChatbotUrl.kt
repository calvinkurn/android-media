package com.tokopedia.chatbot.data.network

import com.tokopedia.network.constant.TkpdBaseURL

/**
 * @author by nisie on 06/12/18.
 */
class ChatbotUrl {
    companion object {
        fun getPathWebsocket(deviceId: String?, userId: String?): String {
            return String.format("%s%s?os_type=1&device_id=%s&user_id=%s",
                    WEBSOCKET_URL,
                    CONNECT_WEBSOCKET,
                    deviceId,
                    userId)
        }

        val CONNECT_WEBSOCKET = "/connect"
        val WEBSOCKET_URL = TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN
    }
}