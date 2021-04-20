package com.tokopedia.chatbot.view.presenter

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.tokopedia.abstraction.base.view.adapter.Visitable
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
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.data.network.ChatbotUrl
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.SHOW_TEXT
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.domain.pojo.livechatdivider.LiveChatDividerAttributes
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitoption.SubmitOptionInput
import com.tokopedia.chatbot.domain.subscriber.*
import com.tokopedia.chatbot.domain.usecase.*
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.CHAT_DIVIDER_DEBUGGING
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.ERROR_CODE
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.LIVE_CHAT_DIVIDER
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.OPEN_CSAT
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.QUERY_SORCE_TYPE
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.UPDATE_TOOLBAR
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
import kotlin.coroutines.CoroutineContext

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
        private val getTickerDataUseCase: GetTickerDataUseCase,
        private val chipSubmitHelpfulQuestionsUseCase: ChipSubmitHelpfulQuestionsUseCase,
        private val chipGetChatRatingListUseCase: ChipGetChatRatingListUseCase,
        private val chipSubmitChatCsatUseCase: ChipSubmitChatCsatUseCase,
        private val getResolutionLinkUseCase: GetResolutionLinkUseCase
) : BaseChatPresenter<ChatbotContract.View>(userSession, chatBotWebSocketMessageMapper), ChatbotContract.Presenter, CoroutineScope {


    object companion{
        const val ERROR_CODE ="400"
        const val TEXT_HIDE = "hide"
        const val OPEN_CSAT = "13"
        const val UPDATE_TOOLBAR = "14"
        const val CHAT_DIVIDER_DEBUGGING = "15"
        const val LIVE_CHAT_DIVIDER = "16"
        const val QUERY_SORCE_TYPE = "Apps"
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
    private val job= SupervisorJob()

    init {
        mSubscription = CompositeSubscription()
        listInterceptor = arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

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
                view.showErrorWebSocket(false)

            }

            override fun onMessage(text: String) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", text)
                }
            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {
                try {
                    if (GlobalConfig.isAllowDebuggingTools()) {
                        Log.d("RxWebSocket Presenter", webSocketResponse.jsonObject.toString())
                    }

                    val pojo: ChatSocketPojo = Gson().fromJson(webSocketResponse.jsonObject, ChatSocketPojo::class.java)
                    if (pojo.msgId.toString() != messageId) return
                    chatResponse = pojo
                    mappingEvent(webSocketResponse, messageId)

                    val attachmentType = chatResponse.attachment?.type

                    if (attachmentType == OPEN_CSAT) {
                        val csatResponse: WebSocketCsatResponse =Gson().fromJson(webSocketResponse.jsonObject,
                                WebSocketCsatResponse::class.java)
                        view.openCsat(csatResponse)
                    }

                    if (attachmentType== UPDATE_TOOLBAR){
                        val tool = Gson().fromJson(chatResponse.attachment?.attributes, ToolbarAttributes::class.java)
                        view.updateToolbar(tool.profileName,tool.profileImage, tool.badgeImage)
                    }

                    val liveChatDividerAttribute = Gson().fromJson(chatResponse.attachment?.attributes, LiveChatDividerAttributes::class.java)
                    if (attachmentType == CHAT_DIVIDER_DEBUGGING) {
                        val model = ChatSepratorViewModel(sepratorMessage = liveChatDividerAttribute?.divider?.label,
                                dividerTiemstamp = chatResponse.message.timeStampUnixNano)
                        view.onReceiveChatSepratorEvent(model, getLiveChatQuickReply())

                    }
                    if(attachmentType == LIVE_CHAT_DIVIDER){
                        mappingQueueDivider(liveChatDividerAttribute, chatResponse.message.timeStampUnixNano)
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
                    view.showErrorWebSocket(true)

                }
            }

            override fun onClose() {
                networkMode = MODE_API

                if (GlobalConfig.isAllowDebuggingTools()) {
                    Log.d("RxWebSocket Presenter", "onClose")
                }
                destroyWebSocket()
                view.showErrorWebSocket(true)
                connectWebSocket(messageId)

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

    private fun mappingQueueDivider(liveChatDividerAttribute: LiveChatDividerAttributes, dividerTime: String) {
        if (!isErrorOnLeaveQueue) {
            val agentQueue = liveChatDividerAttribute.agentQueue
            if (agentQueue?.type.equals(SHOW_TEXT)) {
                view.isBackAllowed(false)
            } else {
                view.isBackAllowed(true)
            }
            val model = ConnectionDividerViewModel(dividerMessage = agentQueue?.label, isShowButton = true,
                    type = agentQueue?.type ?: SHOW_TEXT, leaveQueue = leaveQueue(dividerTime))
            view.onReceiveConnectionEvent(model, getLiveChatQuickReply())
        }

    }

    private fun leaveQueue(dividerTime: String): () -> Unit {
        return {
            leaveQueueUseCase.execute(LeaveQueueUseCase.generateParam(chatResponse.msgId.toString(), Calendar.getInstance().timeInMillis.toString()), LeaveQueueSubscriber(onError(), onSuccess(dividerTime)))
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            view.showErrorToast(it)
        }
    }

    private fun onSuccess(dividerTime: String = ""): (String) -> Unit {
        return { str ->
            if (view != null) {
                view.isBackAllowed(true)
                if (str == ERROR_CODE) isErrorOnLeaveQueue = true
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
                                 onSuccess: (ChatroomViewModel) -> Unit,
                                 onGetChatRatingListMessageError: (String) -> Unit) {
        if (messageId.isNotEmpty()) {
            getExistingChatUseCase.execute(GetExistingChatUseCase.generateParamFirstTime(messageId),
                    GetExistingChatSubscriber(onError, onSuccess, chipGetChatRatingListUseCase, onGetChatRatingListMessageError))
        }
    }

    override fun loadPrevious(messageId: String, page: Int,
                              onError: (Throwable) -> Unit,
                              onSuccess: (ChatroomViewModel) -> Unit,
                              onGetChatRatingListMessageError: (String) -> Unit) {
        if (messageId.isNotEmpty()) {
            getExistingChatUseCase.execute(GetExistingChatUseCase.generateParam(messageId, page),
                    GetExistingChatSubscriber(onError, onSuccess, chipGetChatRatingListUseCase, onGetChatRatingListMessageError))
        }
    }

    override fun mappingEvent(webSocketResponse: WebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo = Gson().fromJson(webSocketResponse.jsonObject, ChatSocketPojo::class.java)
        if (pojo.msgId.toString() != messageId) return

        when (webSocketResponse.code) {
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
                              onError: (Throwable, ImageUploadViewModel) -> Unit) {
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
                            onError(e, it)
                        }

                    })
        }

    }

    override fun cancelImageUpload() {
        uploadImageUseCase.unsubscribe()
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

    override fun hitGqlforOptionList(selectedValue: Int, model: HelpFullQuestionsViewModel?) {
        val input = genrateInput(selectedValue, model)
        chipSubmitHelpfulQuestionsUseCase.execute(chipSubmitHelpfulQuestionsUseCase.generateParam(input), ChipSubmitHelpfullQuestionsSubscriber(onSubmitError))
    }

    private val onSubmitError: (Throwable) -> Unit = {
        it.printStackTrace()
    }

    private fun genrateInput(selectedValue: Int, model: HelpFullQuestionsViewModel?): SubmitOptionInput {
        val input = SubmitOptionInput()
        with(input) {
            caseChatID = model?.helpfulQuestion?.caseChatId ?: ""
            caseID = model?.helpfulQuestion?.caseId ?: ""
            messageID = model?.messageId ?: ""
            source = QUERY_SORCE_TYPE
            value = selectedValue
        }
        return input
    }

    override fun submitChatCsat(input: ChipSubmitChatCsatInput,
                                onsubmitingChatCsatSuccess: (String) -> Unit,
                                onError: (Throwable) -> Unit) {
        chipSubmitChatCsatUseCase.execute(chipSubmitChatCsatUseCase.generateParam(input),
                ChipSubmitChatCsatSubscriber(onsubmitingChatCsatSuccess, onError))
    }

    override fun checkLinkForRedirection(invoiceRefNum: String,
                                         onGetSuccessResponse: (String) -> Unit,
                                         setStickyButtonStatus: (Boolean) -> Unit,
                                         onError: (Throwable) -> Unit) {
        val params = getResolutionLinkUseCase.createRequestParams(invoiceRefNum)
        launchCatchError(
                block = {
                    val orderList =
                            getResolutionLinkUseCase
                                    .getResoLinkResponse(params)
                                    .getResolutionLink?.resolutionLinkData?.orderList?.firstOrNull()
                    if (orderList?.resoList?.isNotEmpty() == true) {
                        setStickyButtonStatus(true)
                    }else{
                        setStickyButtonStatus(false)
                    }
                    val link = orderList?.dynamicLink ?: ""
                    onGetSuccessResponse(getEnvResoLink(link))
                },
                onError = {
                    onError(it)
                }
        )

    }

    private fun getEnvResoLink(link: String): String {
        var url = ""
        if (link.isNotEmpty() && link[0] == '/') {
            url = String.format(TokopediaUrl.getInstance().WEB + "%s", link.removeRange(0, 1))
        }
        return url
    }

    override fun detachView() {
        destroyWebSocket()
        getExistingChatUseCase.unsubscribe()
        sendChatRatingUseCase.unsubscribe()
        sendRatingReasonUseCase.unsubscribe()
        submitCsatRatingUseCase.unsubscribe()
        leaveQueueUseCase.unsubscribe()
        getTickerDataUseCase.unsubscribe()
        chipGetChatRatingListUseCase.unsubscribe()
        chipSubmitHelpfulQuestionsUseCase.unsubscribe()
        chipSubmitChatCsatUseCase.unsubscribe()
        job.cancel()
        super.detachView()
    }

    override fun showTickerData(onError: (Throwable) -> Unit, onSuccesGetTickerData: (TickerData) -> Unit) {
        getTickerDataUseCase.execute(TickerDataSubscriber(onError,onSuccesGetTickerData))
    }

    override fun getActionBubbleforNoTrasaction(): ChatActionBubbleViewModel {
        val text = view.context?.getString(R.string.chatbot_text_for_no_transaction_found) ?: ""
        val value = view.context?.getString(R.string.chatbot_text_for_no_transaction_found) ?: ""
        val action = view.context?.getString(R.string.chatbot_action_text_for_no_transaction_found)
                ?: ""
        return ChatActionBubbleViewModel(text, value, action)
    }
}