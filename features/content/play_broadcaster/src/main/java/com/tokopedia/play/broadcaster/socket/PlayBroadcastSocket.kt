package com.tokopedia.play.broadcaster.socket

import androidx.lifecycle.LiveData
import com.tokopedia.websocket.DEFAULT_DELAY
import com.tokopedia.websocket.DEFAULT_MAX_RETRIES
import com.tokopedia.websocket.DEFAULT_PING


/**
 * Created by mzennis on 24/05/20.
 */
interface PlayBroadcastSocket {

    fun configuration(): PlayBroadcastSocketImpl.SocketConfiguration = PlayBroadcastSocketImpl.SocketConfiguration(
            minReconnectDelay = DEFAULT_DELAY,
            maxRetries = DEFAULT_MAX_RETRIES,
            pingInterval = DEFAULT_PING
    )

    fun socketInfoListener(listener: PlaySocketInfoListener)

    fun connect(channelId: String, groupChatToken: String = "")

    fun destroy()

    fun getObservablePlaySocketMessage(): LiveData<out PlaySocketType>

    companion object {
        const val TAG = "PlayBroadcastSocket"
        const val KEY_GROUP_CHAT_PREFERENCES = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter"
        const val KEY_GROUP_CHAT_PATH = "/ws/groupchat?channel_id="
    }
}