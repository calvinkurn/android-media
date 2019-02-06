package com.tokopedia.topchat.chatlist.data

import com.tokopedia.network.constant.TkpdBaseURL

/**
 * @author by nisie on 27/12/18.
 */
class TopChatUrl {
    companion object {
        fun getPathWebsocket(deviceId: String?, userId: String?): String {
            return String.format("%s?os_type=1&device_id=%s&user_id=%s",
                    WEBSOCKET_URL,
                    deviceId,
                    userId)
        }

        const val WEBSOCKET_URL = "wss://chat.tokopedia.com/connect"
        val BASE_URL = TkpdBaseURL.CHAT_DOMAIN
        val SECURITY_INFO_URL = TkpdBaseURL.WEB_DOMAIN + "panduan-keamanan"

    }
}