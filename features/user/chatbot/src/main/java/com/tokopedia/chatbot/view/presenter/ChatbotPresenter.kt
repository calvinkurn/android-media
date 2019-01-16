package com.tokopedia.chatbot.view.presenter

import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_API
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_WEBSOCKET
import com.tokopedia.chat_common.domain.SendWebsocketParam
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.network.ChatbotUrl
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.subscriber.GetExistingChatSubscriber
import com.tokopedia.chatbot.domain.subscriber.SendRatingReasonSubscriber
import com.tokopedia.chatbot.domain.subscriber.SendRatingSubscriber
import com.tokopedia.chatbot.domain.usecase.GetExistingChatUseCase
import com.tokopedia.chatbot.domain.usecase.SendChatRatingUseCase
import com.tokopedia.chatbot.domain.usecase.SendChatbotWebsocketParam
import com.tokopedia.chatbot.domain.usecase.SendRatingReasonUseCase
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.WebSocket
import okio.ByteString
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.io.File
import javax.inject.Inject

/**
 * @author by nisie on 05/12/18.
 */
class ChatbotPresenter @Inject constructor(
        var getExistingChatUseCase: GetExistingChatUseCase,
        override var userSession: UserSessionInterface,
        private var chatBotWebSocketMessageMapper: ChatBotWebSocketMessageMapper,
        private val tkpdAuthInterceptor: TkpdAuthInterceptor,
        private val fingerprintInterceptor: FingerprintInterceptor,
        private val sendChatRatingUseCase: SendChatRatingUseCase,
        private val sendRatingReasonUseCase: SendRatingReasonUseCase,
        private val uploadImageUseCase: UploadImageUseCase<ChatbotUploadImagePojo>)
    : BaseChatPresenter<ChatbotContract.View>(userSession, chatBotWebSocketMessageMapper), ChatbotContract.Presenter {

    override fun isUploading(): Boolean {
        return isUploading
    }

    private var mSubscription: CompositeSubscription
    private var isUploading: Boolean = false

    private var listInterceptor: ArrayList<Interceptor>

    init {
        mSubscription = CompositeSubscription()
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
    }

    override fun connectWebSocket(messageId: String) {
        val webSocketUrl = ChatbotUrl.getPathWebsocket(userSession.deviceId, userSession.userId)

        destroyWebSocket()

        if (mSubscription.isUnsubscribed) {
            mSubscription = CompositeSubscription()
        }

        val subscriber = object : WebSocketSubscriber() {
            override fun onOpen(webSocket: WebSocket) {
                networkMode = MODE_WEBSOCKET
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
                try {
                    if (GlobalConfig.isAllowDebuggingTools()) {
                        Log.d("RxWebSocket Presenter", "item")
                    }
                    val pojo: ChatSocketPojo = Gson().fromJson(webSocketResponse.getData(), ChatSocketPojo::class.java)
                    if (pojo.msgId.toString() != messageId) return
                    mappingEvent(webSocketResponse, messageId)
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                }
            }

            override fun onMessage(byteString: ByteString) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", byteString.toString())
                }
            }

            override fun onReconnect() {
                networkMode = MODE_WEBSOCKET
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onReconnect")
                }
            }

            override fun onClose() {
                networkMode = MODE_API

                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onClose")
                }
                destroyWebSocket()

            }

        }
        val subscription = RxWebSocket[webSocketUrl, userSession.accessToken, listInterceptor]
                ?.subscribe(subscriber)

        mSubscription.add(subscription)
    }

    override fun showErrorSnackbar(stringId: Int) {
        view.showSnackbarError(stringId)

    }

    override fun sendReadEvent(messageId: String) {
        RxWebSocket.send(SendWebsocketParam.getReadMessage(messageId),
                listInterceptor)
    }

    override fun sendRating(messageId: String, rating: Int, timestamp: String,
                            onError: (Throwable) -> Unit,
                            onSuccess: (SendRatingPojo) -> Unit) {
        sendChatRatingUseCase.execute(SendChatRatingUseCase.generateParam(
                messageId, rating, timestamp), SendRatingSubscriber(onError, onSuccess))
    }

    override fun sendReasonRating(messageId: String, reason: String, timestamp: String,
                                  onError: (Throwable) -> Unit,
                                  onSuccess: (String) -> Unit) {
        sendRatingReasonUseCase.execute(SendRatingReasonUseCase.generateParam(
                messageId, reason, timestamp
        ), SendRatingReasonSubscriber(onError, onSuccess))
    }

    override fun sendActionBubble(messageId: String, selected: ChatActionBubbleViewModel,
                                  startTime: String, opponentId: String) {
        RxWebSocket.send(SendChatbotWebsocketParam.generateParamSendBubbleAction(messageId, selected,
                startTime, opponentId),
                listInterceptor)
    }

    override fun destroyWebSocket() {
        mSubscription.clear()
        mSubscription.unsubscribe()
    }

    override fun getExistingChat(messageId: String,
                                 onError: (Throwable) -> Unit,
                                 onSuccess: (ChatroomViewModel) -> Unit) {
        if (messageId.isNotEmpty()) {
            getExistingChatUseCase.execute(GetExistingChatUseCase.generateParamFirstTime(messageId),
                    GetExistingChatSubscriber(onError, onSuccess))
        }
    }

    override fun loadPrevious(messageId: String, page: Int,
                              onError: (Throwable) -> Unit,
                              onSuccess: (ChatroomViewModel) -> Unit) {
        if (messageId.isNotEmpty()) {
            getExistingChatUseCase.execute(GetExistingChatUseCase.generateParam(messageId, page),
                    GetExistingChatSubscriber(onError, onSuccess))
        }
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

    override fun sendInvoiceAttachment(messageId: String,
                                       invoiceLinkPojo: InvoiceLinkPojo,
                                       startTime: String,
                                       opponentId: String) {
        RxWebSocket.send(SendChatbotWebsocketParam.generateParamSendInvoice(messageId,
                invoiceLinkPojo, startTime, opponentId), listInterceptor)
    }

    override fun sendQuickReply(messageId: String, quickReply: QuickReplyViewModel,
                                startTime: String,
                                opponentId: String) {
        RxWebSocket.send(SendChatbotWebsocketParam.generateParamSendQuickReply(messageId,
                quickReply, startTime, opponentId), listInterceptor)
    }

    override fun sendMessageWithApi(messageId: String, sendMessage: String, startTime: String) {
        //TODO
    }

    override fun sendMessageWithWebsocket(messageId: String, sendMessage: String,
                                          startTime: String, opponentId: String) {
        RxWebSocket.send(SendWebsocketParam.generateParamSendMessage(messageId, sendMessage,
                startTime, opponentId),
                listInterceptor)
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

    override fun uploadImages(it: ImageUploadViewModel,
                              messageId: String,
                              opponentId: String,
                              onError: (Throwable) -> Unit) {
        if (validateImageAttachment(it.imageUrl)) {
            isUploading = true
            uploadImageUseCase.unsubscribe()

            val reqParam = HashMap<String, RequestBody>()
            val webService = RequestBody.create(MediaType.parse("text/plain"), "1")
            reqParam.put("web_service", createRequestBody("1"))
            reqParam.put("id", createRequestBody(String.format("%s%s", userSession.userId, it.imageUrl)))
            val params = uploadImageUseCase.createRequestParam(it.imageUrl,
                    "/upload/attachment",
                    "fileToUpload\"; filename=\"image.jpg",
                    reqParam)

            uploadImageUseCase.execute(params,
                    object : Subscriber<ImageUploadDomainModel<ChatbotUploadImagePojo>>() {
                        override fun onNext(t: ImageUploadDomainModel<ChatbotUploadImagePojo>) {
                            t.dataResultImageUpload.data?.run {
                                sendUploadedImageToWebsocket(SendWebsocketParam
                                        .generateParamSendImage(messageId,
                                                this.picSrc,
                                                it.startTime,
                                                opponentId))
                            }
                            isUploading = false
                        }

                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            isUploading = false
                            onError(e)
                        }

                    })
        }
    }

    private fun sendUploadedImageToWebsocket(json: JsonObject) {
        val list = ArrayList<Interceptor>()
        list.add(tkpdAuthInterceptor)
        list.add(fingerprintInterceptor)

        RxWebSocket.send(json, list)
    }

    private fun createRequestBody(content: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), content)
    }

    private fun validateImageAttachment(uri: String?): Boolean {
        var MAX_FILE_SIZE = 5120
        val MINIMUM_HEIGHT = 100
        val MINIMUM_WIDTH = 300
        val DEFAULT_ONE_MEGABYTE: Long = 1024
        if (uri == null) return false
        val file = File(uri)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth

        val fileSize = Integer.parseInt((file.length() / DEFAULT_ONE_MEGABYTE).toString())

        return if (imageHeight < MINIMUM_HEIGHT || imageWidth < MINIMUM_WIDTH) {
            view.onUploadUndersizedImage()
            false
        } else if (fileSize >= MAX_FILE_SIZE) {
            view.onUploadOversizedImage()
            false
        } else {
            true
        }
    }

    override fun detachView() {
        destroyWebSocket()
        getExistingChatUseCase.unsubscribe()
        sendChatRatingUseCase.unsubscribe()
        sendRatingReasonUseCase.unsubscribe()
        super.detachView()
    }

}