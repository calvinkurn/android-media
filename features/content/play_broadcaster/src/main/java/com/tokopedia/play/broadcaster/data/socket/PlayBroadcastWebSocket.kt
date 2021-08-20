package com.tokopedia.play.broadcaster.data.socket

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject


/**
 * Created by mzennis on 19/08/21.
 */
class PlayBroadcastWebSocket @Inject constructor(
    private val playWebSocket: PlayWebSocket,
    private val cacheHandler: LocalCacheHandler
) : PlayWebSocket by playWebSocket {

    fun connectSocket(channelId: String, gcToken: String) {
        val wsBaseUrl = cacheHandler.getString(
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