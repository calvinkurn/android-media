package com.tokopedia.chatbot.view.presenter

import android.util.Log
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.WebsocketEvent.companion.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.companion.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.companion.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.companion.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.domain.subscriber.GetExistingChatSubscriber
import com.tokopedia.chatbot.domain.usecase.GetExistingChatUseCase
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.WebSocket
import okio.ByteString
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * @author by nisie on 05/12/18.
 */
class ChatbotPresenter @Inject constructor(
        var getExistingChatUseCase: GetExistingChatUseCase,
        var userSession: UserSessionInterface,
        private var chatBotWebSocketMessageMapper: ChatBotWebSocketMessageMapper)
    : BaseDaggerPresenter<ChatbotContract.View>(), ChatbotContract.Presenter {

    private var mSubscription: CompositeSubscription

    init{
        mSubscription = CompositeSubscription()
    }

    override fun connectWebSocket(messageId: String) {
        val webSocketUrl = "wss://chat.tokopedia.com/connect?os_type=1" +
                "&device_id=" + userSession.deviceId +
                "&user_id=" + userSession.userId

        destroyWebSocket()

        if (mSubscription.isUnsubscribed) {
            mSubscription = CompositeSubscription()
        }

        val subscriber = object : WebSocketSubscriber() {
            override fun onOpen(webSocket: WebSocket) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", " on WebSocket open")
                }
            }

            override fun onMessage(text: String) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", text)
                }
            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "item")
                }
                val pojo: ChatSocketPojo = Gson().fromJson(webSocketResponse.getData(), ChatSocketPojo::class.java)
                if (pojo.msgId.toString() != messageId) return
                mappingEvent(webSocketResponse, messageId)
            }

            override fun onMessage(byteString: ByteString) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", byteString.toString())
                }
            }

            override fun onReconnect() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onReconnect")
                }
            }

            override fun onClose() {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onClose")
                }
                destroyWebSocket()

            }

        }

        val subscription = RxWebSocket[webSocketUrl, userSession.accessToken]?.subscribe(subscriber)


        mSubscription.add(subscription)
    }

    override fun destroyWebSocket() {
            mSubscription.clear()
            mSubscription.unsubscribe()
    }

    override fun getExistingChat(messageId: String,
                                 onError: (Throwable) -> Unit,
                                 onSuccess: (ArrayList<Visitable<*>>) -> Unit) {
        getExistingChatUseCase.execute(GetExistingChatUseCase.generateParam(messageId),
                GetExistingChatSubscriber(onError, onSuccess))
    }

    override fun mappingEvent(webSocketResponse: WebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo = Gson().fromJson(webSocketResponse.getData(), ChatSocketPojo::class.java)
        if (pojo.msgId.toString() != messageId) return

        when (webSocketResponse.getCode()) {
            EVENT_TOPCHAT_TYPING -> view.onReceiveStartTypingEvent()
            EVENT_TOPCHAT_END_TYPING -> view.onReceiveStopTypingEvent()
            EVENT_TOPCHAT_READ_MESSAGE -> view.onReceiveReadEvent()
            EVENT_TOPCHAT_REPLY_MESSAGE -> {
                view.onReceiveMessageEvent(mapToVisitable(pojo))
            }
        }
    }

    override fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return chatBotWebSocketMessageMapper.map(pojo)
    }

    override fun sendMessage(sendMessage: String) {
    }

    override fun sendInvoiceAttachment(messageId: String,
                                       invoiceLinkPojo: InvoiceLinkPojo,
                                       startTime: String) {
        // webSocketUseCase.execute(webSocketUseCase.getParamSendInvoiceAttachment(messageId,
//        invoice, startTime));
    }

    override fun sendQuickReply(messageId: String, quickReply: QuickReplyViewModel,
                                generateStartTime: String) {

    }

    override fun generateInvoice(invoiceLinkPojo: InvoiceLinkPojo, senderId: String):
            AttachInvoiceSentViewModel {
        val invoiceLinkAttributePojo = invoiceLinkPojo.attributes
        return AttachInvoiceSentViewModel(
                senderId,
                userSession.name,
                invoiceLinkAttributePojo.title,
                invoiceLinkAttributePojo.description,
                invoiceLinkAttributePojo.imageUrl,
                invoiceLinkAttributePojo.totalAmount,
                SendableViewModel.generateStartTime()
        )
    }

    override fun detachView() {
        destroyWebSocket()
        super.detachView()
    }
}