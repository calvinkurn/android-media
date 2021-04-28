package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.chatlist.domain.websocket.PendingMessageHandler
import com.tokopedia.topchat.chatlist.domain.websocket.WebSocketParser
import com.tokopedia.topchat.chatlist.domain.websocket.WebSocketStateHandler
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatListWebSocketViewModel @Inject constructor(
        webSocket: DefaultTopChatWebSocket,
        webSocketParser: WebSocketParser,
        webSocketStateHandler: WebSocketStateHandler,
        private val userSession: UserSessionInterface,
        dispatchers: CoroutineDispatchers,
        private val pendingMessageHandler: PendingMessageHandler
) : WebSocketViewModel(
        webSocket,
        webSocketParser,
        webSocketStateHandler,
        dispatchers
), LifecycleObserver {

    val pendingMessages get() = pendingMessageHandler.pendingMessages
    var role: Int = RoleType.BUYER
    var activeRoom: String = ""

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onLifeCycleStart() {
        isOnStop = false
        activeRoom = ""
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onLifeCycleStop() {
        isOnStop = true
    }

    override fun shouldStopReceiveEventOnStop(): Boolean {
        return false
    }

    override fun onResponseReplyMessage(response: WebSocketResponse) {
        val data = mapToIncomingChat(response)
        val chat = Success(data)
        if (!isOnStop && !pendingMessageHandler.hasPendingMessage()) {
            _itemChat.postValue(chat)
        } else {
            launch(dispatchers.main) {
                queueIncomingMessage(data)
            }
        }
    }

    private fun queueIncomingMessage(data: IncomingChatWebSocketModel) {
        val isReplyFromActiveRoom = data.messageId == activeRoom
                && !data.isFromMySelf(role, userSession.userId)
        pendingMessageHandler.addQueue(role, data, isReplyFromActiveRoom)
    }

    fun onRoleChanged(role: Int) {
        this.role = role
    }

    fun clearPendingMessages() {
        pendingMessages.clear()
    }

    fun deletePendingMsgWithId(msgId: String) {
        pendingMessages.remove(msgId)
    }

}