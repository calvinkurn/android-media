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
            KEY_GROUP_CHAT_PREFERENCES,
            TokopediaUrl.getInstance().WS_PLAY
        )
        val wsFullUrl = buildString {
            append("$wsBaseUrl$KEY_GROUP_CHAT_PATH$channelId")
            if (gcToken.isNotEmpty()) append("&token=$gcToken")
        }

        playWebSocket.connect(wsFullUrl)
    }

    companion object {
        const val KEY_GROUP_CHAT_PREFERENCES = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter"
        private const val KEY_GROUP_CHAT_PATH = "/ws/groupchat?channel_id="
    }
}