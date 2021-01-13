package com.tokopedia.topchat.chatlist.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_TYPING
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponseData
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesContactPojo
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatCoroutineContextProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

open class WebSocketViewModel @Inject constructor(
        protected val dispatchers: TopchatCoroutineContextProvider,
        protected val webSocket: TopChatWebSocket
) : BaseViewModel(dispatchers.ioDispatcher), LifecycleObserver {

    protected val _itemChat = MutableLiveData<Result<BaseIncomingItemWebSocketModel>>()
    val itemChat: LiveData<Result<BaseIncomingItemWebSocketModel>>
        get() = _itemChat

    protected var isOnStop = false

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

    protected fun mapToIncomingChat(response: WebSocketResponse): IncomingChatWebSocketModel {
        val json = response.jsonObject
        val responseData = Gson().fromJson(json, WebSocketResponseData::class.java)
        val msgId = responseData.msgId.toString()
        val message = responseData.message.censoredReply.trim().toEmptyStringIfNull()
        val time = responseData.message.timeStampUnix.toEmptyStringIfNull()

        val contact = ItemChatAttributesContactPojo(
                responseData?.fromUid.toString(),
                responseData?.fromRole.toString(),
                "",
                responseData?.from.toString(),
                0,
                responseData?.fromRole.toString(),
                responseData?.imageUri.toString(),
                responseData.isAutoReply
        )
        return IncomingChatWebSocketModel(msgId, message, time, contact)
    }

    protected fun mapToIncomingTypeState(response: WebSocketResponse, isTyping: Boolean): IncomingTypingWebSocketModel {
        val json = response.jsonObject
        val responseData = Gson().fromJson(json, WebSocketResponseData::class.java)
        val msgId = responseData?.msgId.toString()

        val contact = ItemChatAttributesContactPojo(
                responseData?.fromUid.toString(),
                responseData?.fromRole.toString(),
                "",
                responseData?.from.toString(),
                0,
                responseData?.fromRole.toString(),
                ""
        )

        return IncomingTypingWebSocketModel(msgId, isTyping, contact)
    }

    override fun onCleared() {
        super.onCleared()
        webSocket.cancelChannel()
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
