package com.tokopedia.chatbot.view.presenter

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
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
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceLinkPojo
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.data.network.ChatbotUrl
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.SHOW_TEXT
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.domain.pojo.livechatdivider.LiveChatDividerAttributes
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.domain.subscriber.*
import com.tokopedia.chatbot.domain.usecase.*
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.CHAT_DIVIDER_DEBUGGING
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.ERROR_CODE
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.LIVE_CHAT_DIVIDER
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.OPEN_CSAT
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.TEXT_HIDE
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.UPDATE_TOOLBAR
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
import java.util.Calendar
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        private val uploadImageUseCase: UploadImageUseCase<ChatbotUploadImagePojo>,
        private val submitCsatRatingUseCase: SubmitCsatRatingUseCase,
        private val leaveQueueUseCase: LeaveQueueUseCase,
        private val getTickerDataUseCase: GetTickerDataUseCase
) : BaseChatPresenter<ChatbotContract.View>(userSession, chatBotWebSocketMessageMapper), ChatbotContract.Presenter {


    object companion{
        const val ERROR_CODE ="400"
        const val TEXT_HIDE = "hide"
        const val OPEN_CSAT = "13"
        const val UPDATE_TOOLBAR = "14"
        const val CHAT_DIVIDER_DEBUGGING = "15"
        const val LIVE_CHAT_DIVIDER = "16"
    }

    override fun submitCsatRating(inputItem: InputItem, onError: (Throwable) -> Unit, onSuccess: (String) -> Unit) {
        submitCsatRatingUseCase.execute(SubmitCsatRatingUseCase.generateParam(inputItem
        ), SubmitCsatRatingSubscriber(onError, onSuccess))
    }

    override fun clearText() {
        view.clearChatText()
    }

    override fun isUploading(): Boolean {
        return isUploading
    }

    private var mSubscription: CompositeSubscription
    private var isUploading: Boolean = false
    private var listInterceptor: ArrayList<Interceptor>
    private var isErrorOnLeaveQueue = false
    private lateinit var chatResponse:ChatSocketPojo

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
                        Log.d("RxWebSocket Presenter", webSocketResponse.getData().toString())
                    }

                    val pojo: ChatSocketPojo = Gson().fromJson(webSocketResponse.getData(), ChatSocketPojo::class.java)
                    if (pojo.msgId.toString() != messageId) return
                    chatResponse = pojo
                    mappingEvent(webSocketResponse, messageId)

                    val attachmentType = chatResponse.attachment?.type

                    if (attachmentType == OPEN_CSAT) {
                        val csatResponse: WebSocketCsatResponse =Gson().fromJson(webSocketResponse.getData(),
                                WebSocketCsatResponse::class.java)
                        view.openCsat(csatResponse)
                    }

                    if (attachmentType== UPDATE_TOOLBAR){
                        val tool = Gson().fromJson(chatResponse.attachment?.attributes, ToolbarAttributes::class.java)
                        view.updateToolbar(tool.profileName,tool.profileImage)
                    }

                    val liveChatDividerAttribute = Gson().fromJson(chatResponse.attachment?.attributes, LiveChatDividerAttributes::class.java)
                    if (attachmentType == CHAT_DIVIDER_DEBUGGING) {
                        val model = ConnectionDividerViewModel(liveChatDividerAttribute?.divider?.label, false, SHOW_TEXT, null)
                        view.onReceiveConnectionEvent(model, getLiveChatQuickReply())
                    }
                    if(attachmentType == LIVE_CHAT_DIVIDER){
                        mappingQueueDivider(liveChatDividerAttribute)
                    }

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

    private fun getLiveChatQuickReply(): List<QuickReplyViewModel> {
        val quickReplyListPojo = GsonBuilder().create()
                .fromJson<QuickReplyAttachmentAttributes>(chatResponse.attachment?.attributes,
                        QuickReplyAttachmentAttributes::class.java)
        val list = ArrayList<QuickReplyViewModel>()
        if (quickReplyListPojo != null && !quickReplyListPojo.quickReplies.isEmpty()) {
            for (pojo in quickReplyListPojo.quickReplies) {
                if (!TextUtils.isEmpty(pojo.text)) {
                    list.add(QuickReplyViewModel(pojo.text, pojo.value, pojo.action))
                }
            }
        }
        return list
    }

    private fun mappingQueueDivider(liveChatDividerAttribute: LiveChatDividerAttributes) {
        if (!isErrorOnLeaveQueue) {
            val agentQueue = liveChatDividerAttribute.agentQueue
            if (agentQueue?.type.equals(SHOW_TEXT)) {
                view.isBackAllowed(false)
            } else {
                view.isBackAllowed(true)
            }
            val model = ConnectionDividerViewModel(agentQueue?.label, true,
                    agentQueue?.type ?: SHOW_TEXT, leaveQueue())
            view.onReceiveConnectionEvent(model,getLiveChatQuickReply())
        }

    }

    private fun leaveQueue(): () -> Unit {
        return {
            leaveQueueUseCase.execute(LeaveQueueUseCase.generateParam(chatResponse.msgId.toString(), Calendar.getInstance().timeInMillis.toString()), LeaveQueueSubscriber(onError(), onSuccess()))
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            view.showErrorToast(it)
        }
    }

    private fun onSuccess(): (String) -> Unit {
        return { str ->
            if (view!=null){
                view.isBackAllowed(true)
                if(str==ERROR_CODE){
                    isErrorOnLeaveQueue = true
                    val model = ConnectionDividerViewModel("",false, TEXT_HIDE, null)
                    view.onReceiveConnectionEvent(model, getLiveChatQuickReply())
                }else{
                    val model = ConnectionDividerViewModel(view.context?.getString(R.string.cb_bot_you_left_the_queue),false, SHOW_TEXT, null)
                    view.onReceiveConnectionEvent(model, getLiveChatQuickReply())
                }
            }
        }
    }

    override fun showErrorSnackbar(stringId: Int) {
        view.showSnackbarError(stringId)

    }

    override fun sendReadEvent(messageId: String) {
        RxWebSocket.send(SendWebsocketParam.getReadMessage(messageId),
                listInterceptor)
    }

    private fun sendReadEventWebSocket(messageId: String) {
        RxWebSocket.send(getReadMessageWebSocket(messageId),
                listInterceptor)
    }

    private fun getReadMessageWebSocket(messageId: String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(messageId))
        json.add("data", data)
        return json
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
                    sendReadEventWebSocket(messageId)
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

    fun OnClickLeaveQueue() {
        leaveQueueUseCase.execute(LeaveQueueUseCase.generateParam(chatResponse.msgId.toString(), Calendar.getInstance().timeInMillis.toString()), LeaveQueueSubscriber(onError(), onSuccess()))
    }

    override fun detachView() {
        destroyWebSocket()
        getExistingChatUseCase.unsubscribe()
        sendChatRatingUseCase.unsubscribe()
        sendRatingReasonUseCase.unsubscribe()
        submitCsatRatingUseCase.unsubscribe()
        leaveQueueUseCase.unsubscribe()
        getTickerDataUseCase.unsubscribe()
        super.detachView()
    }

    override fun showTickerData(onError: (Throwable) -> Unit, onSuccesGetTickerData: (TickerData) -> Unit) {
        getTickerDataUseCase.execute(TickerDataSubscriber(onError,onSuccesGetTickerData))
    }
}