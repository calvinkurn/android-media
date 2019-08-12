package com.tokopedia.topchat.chatlist.viewmodel

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.kotlin.extensions.view.debug
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_TYPING
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponseData
import com.tokopedia.topchat.chatlist.model.BaseIncomingItemWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesContactPojo
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import javax.inject.Inject

/**
 * Created by stevenfredian on 10/19/17.
 */

class WebSocketViewModel
@Inject constructor(dispatcher: CoroutineDispatcher,
                    private val userSession: UserSession,
                    private val tkpdAuthInterceptor: TkpdAuthInterceptor,
                    private val fingerprintInterceptor: FingerprintInterceptor) : BaseViewModel(dispatcher), LifecycleObserver {

    val client = OkHttpClient()
    private val webSocketUrl: String = ChatUrl.CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
            "?os_type=1" +
            "&device_id=" + userSession.deviceId +
            "&user_id=" + userSession.userId
    val uiScope = CoroutineScope(coroutineContext)
    private var easyWS: EasyWS? = null

    val itemChat = MutableLiveData<Result<BaseIncomingItemWebSocketModel>>()

    fun connectWebSocket() {
        uiScope.launch {
            client.run { newBuilder().addInterceptor(tkpdAuthInterceptor)
                                     .addInterceptor(fingerprintInterceptor) }
            easyWS = client.easyWebSocket(webSocketUrl, userSession.accessToken)

            debug("tevWS"," Open: ${easyWS?.response}")

            easyWS?.let {
                for (response in it.textChannel) {
                    debug("tevWS"," Response: $response")
                    when(response.getCode()) {
                        EVENT_TOPCHAT_REPLY_MESSAGE -> itemChat.value = Success(mapToIncomingChat(response))
                        EVENT_TOPCHAT_TYPING -> itemChat.value = Success(mapToIncomingTypeState(response, true))
                        EVENT_TOPCHAT_END_TYPING -> itemChat.value = Success(mapToIncomingTypeState(response, false))
                    }
                }
            }
        }
    }

    private fun mapToIncomingChat(response: WebSocketResponse): IncomingChatWebSocketModel {
        val json = response.getData()
        val responseData = Gson().fromJson(json, WebSocketResponseData::class.java)
        val msgId = responseData.msgId.toString()
        val message = responseData.message.censoredReply.trim()

        var contact = ItemChatAttributesContactPojo(
                responseData.fromUid.toString(),
                responseData.fromRole,
                "",
                responseData.from,
                0,
                responseData.fromRole,
                responseData.imageUri
        )
        return IncomingChatWebSocketModel(msgId, message, "", contact)
    }

    private fun mapToIncomingTypeState(response: WebSocketResponse, isTyping: Boolean): IncomingTypingWebSocketModel {
        val json = response.getData()
        val responseData = Gson().fromJson(json, WebSocketResponseData::class.java)
        val msgId = responseData.msgId.toString()
        return IncomingTypingWebSocketModel(msgId, isTyping)
    }

    override fun onCleared() {
        super.onCleared()
        easyWS?.webSocket?.close(1000, "Bye!")
        debug("tevWS"," OnCleared")
    }

}
