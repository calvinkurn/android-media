package com.tokopedia.topchat.chatlist.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.kotlin.extensions.view.debug
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.topchat.chatlist.data.ChatListWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.topchat.chatlist.domain.pojo.message.Contact
import com.tokopedia.topchat.chatlist.domain.pojo.message.ContactAttributes
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponseData
import com.tokopedia.topchat.chatlist.model.IncomingItemWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
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
                    private val fingerprintInterceptor: FingerprintInterceptor) : BaseViewModel(dispatcher) {

    val client = OkHttpClient()
    private val webSocketUrl: String = ChatUrl.CHAT_WEBSOCKET_DOMAIN + ChatUrl.CONNECT_WEBSOCKET +
            "?os_type=1" +
            "&device_id=" + userSession.deviceId +
            "&user_id=" + userSession.userId
    val uiScope = CoroutineScope(coroutineContext)
    private var easyWS: EasyWS? = null

    val itemChat = MutableLiveData<Result<IncomingItemWebSocketModel>>()

    fun connectWebSocket() {
        uiScope.launch {
            client.run { newBuilder().addInterceptor(tkpdAuthInterceptor)
                                     .addInterceptor(fingerprintInterceptor) }
            easyWS = client.easyWebSocket(webSocketUrl, userSession.accessToken)

            debug("tevWS"," Open: ${easyWS?.response}")

            easyWS?.let {
                for (response in it.textChannel) {
                    when(response.getCode()) {
                        EVENT_TOPCHAT_REPLY_MESSAGE -> itemChat.value = Success(mapToItemChat(response))
                    }
                }
            }
        }
    }

//    private fun onErrorGetChatListMessage(): (Throwable) -> Unit {
//        return {
//            it.printStackTrace()
//            mutateChatListResponse.value = Fail(it)
//        }
//    }
//
//    private fun onSuccessGetChatListMessage(page: Int): (ChatListPojo) -> Unit {
//        return {
//            mutateChatListResponse.value = Success(it)
//        }
//    }

    private fun mapToItemChat(response: WebSocketResponse): IncomingItemWebSocketModel {
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
        return IncomingItemWebSocketModel(msgId, message, "", contact)
    }

    override fun onCleared() {
        super.onCleared()
        easyWS?.webSocket?.close(1000, "Bye!")
        debug("tevWS"," OnCleared")
    }

}
