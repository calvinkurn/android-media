package com.tokopedia.play.broadcaster.data.socket

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse
import javax.inject.Inject


/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastSocket @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
) {

    var channelId: String = ""
    var gcToken: String = ""

    fun connect(onMessageReceived: (WebSocketResponse)-> Unit, onReconnect: () -> Unit, onError: (error: Throwable) -> Unit) {
    }

    fun send(message: String) {

    }

    fun destroy() {

    }

    companion object {
        const val KEY_GROUPCHAT_PREFERENCES = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter"
    }
}