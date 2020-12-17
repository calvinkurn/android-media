package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.inboxcommon.RoleType
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant
import com.tokopedia.topchat.chatlist.domain.websocket.PendingMessageHandler
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatListWebSocketViewModel @Inject constructor(
        dispatchers: TopchatCoroutineContextProvider,
        userSession: UserSessionInterface,
        tkpdAuthInterceptor: TkpdAuthInterceptor,
        fingerprintInterceptor: FingerprintInterceptor,
        private val pendingMessageHandler: PendingMessageHandler
) : WebSocketViewModel(
        dispatchers,
        userSession,
        tkpdAuthInterceptor,
        fingerprintInterceptor
), LifecycleObserver {

    val pendingMessages get() = pendingMessageHandler.pendingMessages

    var role: Int = RoleType.BUYER

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun connectListener() {
        isOnStop = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        isOnStop = true
    }


    override fun connectWebSocket() {
        launch {
            client.run {
                newBuilder().addInterceptor(tkpdAuthInterceptor)
                        .addInterceptor(fingerprintInterceptor)
            }
            easyWS = client.easyWebSocket(webSocketUrl, userSession.accessToken)
            easyWS?.let {
                for (response in it.textChannel) {
                    when (response.code) {
                        ChatListWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE -> {
                            val data = mapToIncomingChat(response)
                            val chat = Success(data)
                            if (!isOnStop && !pendingMessageHandler.hasPendingMessage()) {
                                _itemChat.postValue(chat)
                            } else {
                                withContext(dispatchers.Main) {
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
    }

    private fun queueIncomingMessage(data: IncomingChatWebSocketModel) {
        pendingMessageHandler.addQueue(data, role)
    }

    fun onRoleChanged(role: Int) {
        this.role = role
    }

}