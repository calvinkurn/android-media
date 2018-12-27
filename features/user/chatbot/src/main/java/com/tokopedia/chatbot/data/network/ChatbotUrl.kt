package com.tokopedia.chatbot.data.network

/**
 * @author by nisie on 06/12/18.
 */
class ChatbotUrl {
    companion object {
        fun getPathWebsocket(deviceId: String?, userId: String?): String {
            return String.format("%s?os_type=1&device_id=%s&user_id=%s",
                    WEBSOCKET_URL,
                    deviceId,
                    userId)
        }

        const val WEBSOCKET_URL = "wss://chat.tokopedia.com/connect"
    }
}