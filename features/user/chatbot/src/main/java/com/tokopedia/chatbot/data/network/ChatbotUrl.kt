package com.tokopedia.chatbot.data.network

import com.tokopedia.url.TokopediaUrl
import java.util.*

/**
 * @author by nisie on 06/12/18.
 */
class ChatbotUrl {
    companion object {
        fun getPathWebsocket(deviceId: String?, userId: String?): String {
            return String.format(
                Locale.getDefault(),
                "%s%s?os_type=1&device_id=%s&user_id=%s&source=chatbot",
                WEBSOCKET_URL,
                CONNECT_WEBSOCKET,
                deviceId,
                userId
            )
        }

        private const val CONNECT_WEBSOCKET = "/connect"
        val WEBSOCKET_URL = TokopediaUrl.getInstance().WS_CHAT
    }
}
