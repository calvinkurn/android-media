package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingTypeState
import com.tokopedia.topchat.chatlist.domain.websocket.PendingMessageHandler
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatListWebSocketViewModel @Inject constructor(
        webSocket: DefaultTopChatWebSocket,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers,
        private val pendingMessageHandler: PendingMessageHandler
) : WebSocketViewModel(
        webSocket,
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

    override fun connectWebSocket() {
        launch {
            for (response in webSocket.createWebSocket()) {
                when (response.code) {
                    ChatListWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE -> {
                        val data = mapToIncomingChat(response)
                        val chat = Success(data)
                        if (!isOnStop && !pendingMessageHandler.hasPendingMessage()) {
                            _itemChat.postValue(chat)
                        } else {
                            withContext(dispatchers.main) {
                                queueIncomingMessage(data)
                            }
                        }
                    }
                    ChatListWebSocketConstant.EVENT_TOPCHAT_TYPING -> {
                        val stateTyping = Success(mapToIncomingTypeState(response, true))
                        _itemChat.postValue(stateTyping)
                    }
                    ChatListWebSocketConstant.EVENT_TOPCHAT_END_TYPING -> {
                        val stateEndTyping = Success(mapToIncomingTypeState(response, false))
                        _itemChat.postValue(stateEndTyping)
                    }
                }
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