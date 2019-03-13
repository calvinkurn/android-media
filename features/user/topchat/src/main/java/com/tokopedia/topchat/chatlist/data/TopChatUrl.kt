package com.tokopedia.topchat.chatlist.data

import com.tokopedia.network.constant.TkpdBaseURL

/**
 * @author by nisie on 27/12/18.
 */
class TopChatUrl {
    companion object {
        fun getPathWebsocket(deviceId: String?, userId: String?): String {
            return String.format("%s%s?os_type=1&device_id=%s&user_id=%s",
                    WEBSOCKET_URL,
                    CONNECT_WEBSOCKET,
                    deviceId,
                    userId)
        }

        val WEBSOCKET_URL = TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN
        val CONNECT_WEBSOCKET = "/connect"
        val BASE_URL = TkpdBaseURL.CHAT_DOMAIN
        val SECURITY_INFO_URL = TkpdBaseURL.WEB_DOMAIN + "panduan-keamanan"

    }
}