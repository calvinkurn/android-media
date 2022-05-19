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
        playWebSocket.connect(channelId, gcToken, SOURCE_PLAY_BROADCASTER)
    }

    companion object {
        private const val PLAY_WEB_SOCKET_GROUP_CHAT = "/ws/groupchat?channel_id="

        private const val KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES = "ip_groupchat"

        private const val SOURCE_PLAY_BROADCASTER = "Broadcaster"
    }
}