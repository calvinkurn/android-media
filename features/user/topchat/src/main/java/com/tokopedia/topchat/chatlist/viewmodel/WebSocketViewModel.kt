package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_TYPING
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingChat
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper.mapToIncomingTypeState
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

open class WebSocketViewModel @Inject constructor(
        protected val webSocket: DefaultTopChatWebSocket,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io), LifecycleObserver {

    protected val _itemChat = MutableLiveData<Result<BaseIncomingItemWebSocketModel>>()
    val itemChat: LiveData<Result<BaseIncomingItemWebSocketModel>>
        get() = _itemChat

    var isOnStop = false

    open fun connectWebSocket() {
        launch {
            for (response in webSocket.createWebSocket()) {
                Timber.d(" Response: $response")
                if (isOnStop) continue
                when (response.code) {
                    EVENT_TOPCHAT_REPLY_MESSAGE -> {
                        val chat = Success(mapToIncomingChat(response))
                        _itemChat.postValue(chat)
                    }
                    EVENT_TOPCHAT_TYPING -> {
                        val stateTyping = Success(mapToIncomingTypeState(response, true))
                        _itemChat.postValue(stateTyping)
                    }
                    EVENT_TOPCHAT_END_TYPING -> {
                        val stateEndTyping = Success(mapToIncomingTypeState(response, false))
                        _itemChat.postValue(stateEndTyping)
                    }
                }
            }
        }
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
