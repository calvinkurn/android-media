package com.tokopedia.play.data.websocket

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.data.websocket.revamp.PlayWebSocket
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject

/**
 * Created by jegul on 09/03/21
 */
class PlayChannelWebSocket @Inject constructor(
        private val playWebSocket: PlayWebSocket,
        private val localCacheHandler: LocalCacheHandler
) : PlayWebSocket by playWebSocket {

    fun connectSocket(channelId: String, gcToken: String) {
        val wsBaseUrl = localCacheHandler.getString(
                KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES,
                TokopediaUrl.getInstance().WS_PLAY
        )
        val wsFullUrl = buildString {
            append("$wsBaseUrl$PLAY_WEB_SOCKET_GROUP_CHAT$channelId")
            if (gcToken.isNotEmpty()) append("&token=$gcToken")
        }

        playWebSocket.connect(wsFullUrl)
    }

    companion object {

        private const val PLAY_WEB_SOCKET_GROUP_CHAT = "/ws/groupchat?channel_id="

        private const val KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES = "ip_groupchat"
    }
}