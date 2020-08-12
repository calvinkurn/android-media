package com.tokopedia.play.broadcaster.socket


/**
 * Created by mzennis on 24/05/20.
 */
interface PlayBroadcastSocket {

    fun config(minReconnectDelay: Int,
               maxRetries: Int,
               pingInterval: Long)

    fun socketInfoListener(listener: PlaySocketInfoListener)

    fun connect(channelId: String, groupChatToken: String = "")

    fun close()

    fun destroy()

    companion object {
        const val TAG = "PlayBroadcastSocket"
        const val KEY_GROUP_CHAT_PREFERENCES = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter"
        const val KEY_GROUP_CHAT_PATH = "/ws/groupchat?channel_id="
    }
}