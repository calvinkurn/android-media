package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_TYPING
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingTypeState
import com.tokopedia.topchat.chatlist.domain.websocket.DefaultTopChatWebSocket
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.topchat.chatlist.domain.websocket.WebSocketParser
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.websocket.WebSocketResponse
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

open class WebSocketViewModel @Inject constructor(
        protected val webSocket: DefaultTopChatWebSocket,
        protected val webSocketParser: WebSocketParser,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io), LifecycleObserver {

    protected val _itemChat = MutableLiveData<Result<BaseIncomingItemWebSocketModel>>()
    val itemChat: LiveData<Result<BaseIncomingItemWebSocketModel>>
        get() = _itemChat

    var isOnStop = false

    protected open fun shouldStopReceiveEventOnStop(): Boolean {
        return true
    }

    fun connectWebSocket() {
        webSocket.connectWebSocket(object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val response = webSocketParser.parseResponse(text)
                handleOnMessageWebSocket(response)
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Timber.d("$TAG - onFailure - ${t.message}")
            }
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("$TAG - onClosed - $code - $reason")
            }
            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Timber.d("$TAG - onMessage - bytes")
            }
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Timber.d("$TAG - onOpen")
            }
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("$TAG - onClosing - $code - $reason")
            }
        })
    }

    private fun handleOnMessageWebSocket(response: WebSocketResponse) {
        if (shouldStopReceiveEventOnStop() && isOnStop) return
        when (response.code) {
            EVENT_TOPCHAT_REPLY_MESSAGE -> onResponseReplyMessage(response)
            EVENT_TOPCHAT_TYPING -> onResponseTyping(response)
            EVENT_TOPCHAT_END_TYPING -> onResponseEndTyping(response)
        }
    }

    protected open fun onResponseReplyMessage(response: WebSocketResponse) {
        val chat = Success(mapToIncomingChat(response))
        _itemChat.postValue(chat)
    }

    private fun onResponseTyping(response: WebSocketResponse) {
        val stateTyping = Success(mapToIncomingTypeState(response, true))
        _itemChat.postValue(stateTyping)
    }

    private fun onResponseEndTyping(response: WebSocketResponse) {
        val stateEndTyping = Success(mapToIncomingTypeState(response, false))
        _itemChat.postValue(stateEndTyping)
    }

    override fun onCleared() {
        super.onCleared()
        webSocket.cancel()
        Timber.d(" OnCleared")
    }

    fun clearItemChatValue() {
        _itemChat.value = null
    }

    fun onStop() {
        isOnStop = true
    }

    fun onStart() {
        isOnStop = false
    }

    companion object {
        const val TAG = "WebSocketViewModel"
    }

}
