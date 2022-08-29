package com.tokopedia.chatbot.view.presenter

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.SendableUiModel.Companion.SENDING_TEXT
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_API
import com.tokopedia.chat_common.data.WebsocketEvent.Mode.MODE_WEBSOCKET
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ARTICLE_ID
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ARTICLE_TITLE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.CODE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.CREATE_TIME
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.DESCRIPTION
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.EVENT
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.ID
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.IMAGE_URL
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.IS_ATTACHED
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.STATUS
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.STATUS_COLOR
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.STATUS_ID
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.TITLE
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.TOTAL_AMOUNT
import com.tokopedia.chatbot.ChatbotConstant.ChatbotUnification.USED_BY
import com.tokopedia.chatbot.ChatbotConstant.ImageUpload.DEFAULT_ONE_MEGABYTE
import com.tokopedia.chatbot.ChatbotConstant.ImageUpload.MAX_FILE_SIZE
import com.tokopedia.chatbot.ChatbotConstant.ImageUpload.MAX_FILE_SIZE_UPLOAD_SECURE
import com.tokopedia.chatbot.ChatbotConstant.ImageUpload.MINIMUM_HEIGHT
import com.tokopedia.chatbot.ChatbotConstant.ImageUpload.MINIMUM_WIDTH
import com.tokopedia.chatbot.ChatbotConstant.MODE_AGENT
import com.tokopedia.chatbot.ChatbotConstant.MODE_BOT
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.imageupload.ChatbotUploadImagePojo
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleViewModel
import com.tokopedia.chatbot.data.network.ChatbotUrl
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.replybubble.ReplyBubbleAttributes
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.data.uploadsecure.UploadSecureResponse
import com.tokopedia.chatbot.domain.ChatbotSendWebsocketParam
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.SHOW_TEXT
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.domain.pojo.livechatdivider.LiveChatDividerAttributes
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitoption.SubmitOptionInput
import com.tokopedia.chatbot.domain.subscriber.ChipSubmitChatCsatSubscriber
import com.tokopedia.chatbot.domain.subscriber.ChipSubmitHelpfullQuestionsSubscriber
import com.tokopedia.chatbot.domain.subscriber.LeaveQueueSubscriber
import com.tokopedia.chatbot.domain.subscriber.SendRatingReasonSubscriber
import com.tokopedia.chatbot.domain.subscriber.SendRatingSubscriber
import com.tokopedia.chatbot.domain.subscriber.SubmitCsatRatingSubscriber
import com.tokopedia.chatbot.domain.subscriber.TickerDataSubscriber
import com.tokopedia.chatbot.domain.usecase.ChatBotSecureImageUploadUseCase
import com.tokopedia.chatbot.domain.usecase.CheckUploadSecureUseCase
import com.tokopedia.chatbot.domain.usecase.ChipGetChatRatingListUseCase
import com.tokopedia.chatbot.domain.usecase.ChipSubmitChatCsatUseCase
import com.tokopedia.chatbot.domain.usecase.ChipSubmitHelpfulQuestionsUseCase
import com.tokopedia.chatbot.domain.usecase.GetExistingChatUseCase
import com.tokopedia.chatbot.domain.usecase.GetResolutionLinkUseCase
import com.tokopedia.chatbot.domain.usecase.GetTickerDataUseCase
import com.tokopedia.chatbot.domain.usecase.GetTopBotNewSessionUseCase
import com.tokopedia.chatbot.domain.usecase.LeaveQueueUseCase
import com.tokopedia.chatbot.domain.usecase.SendChatRatingUseCase
import com.tokopedia.chatbot.domain.usecase.SendChatbotWebsocketParam
import com.tokopedia.chatbot.domain.usecase.SendRatingReasonUseCase
import com.tokopedia.chatbot.domain.usecase.SubmitCsatRatingUseCase
import com.tokopedia.chatbot.util.convertMessageIdToLong
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.CHAT_DIVIDER_DEBUGGING
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.ERROR_CODE
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.LIVE_CHAT_DIVIDER
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.OPEN_CSAT
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.QUERY_SORCE_TYPE
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.SESSION_CHANGE
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.UPDATE_TOOLBAR
import com.tokopedia.chatbot.view.util.isInDarkMode
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.WebSocket
import okio.ByteString
import rx.Subscriber
import rx.subscriptions.CompositeSubscription
import java.io.File
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject
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
        private val getResolutionLinkUseCase: GetResolutionLinkUseCase,
        private val getTopBotNewSessionUseCase: GetTopBotNewSessionUseCase,
        private val checkUploadSecureUseCase: CheckUploadSecureUseCase,
        private val chatBotSecureImageUploadUseCase:ChatBotSecureImageUploadUseCase,
        private val getExistingChatMapper: ChatbotGetExistingChatMapper
) : BaseChatPresenter<ChatbotContract.View>(userSession, chatBotWebSocketMessageMapper), ChatbotContract.Presenter, CoroutineScope {


    object companion{
        const val ERROR_CODE ="400"
        const val TEXT_HIDE = "hide"
        const val OPEN_CSAT = "13"
        const val UPDATE_TOOLBAR = "14"
        const val CHAT_DIVIDER_DEBUGGING = "15"
        const val LIVE_CHAT_DIVIDER = "16"
        const val QUERY_SORCE_TYPE = "Apps"
        const val SESSION_CHANGE = "31"
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
                sendReadEventWebSocket(messageId)
                view.showErrorWebSocket(false)
                view.sendInvoiceForArticle()
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
                        updateToolbar(tool)
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

                    if(attachmentType == SESSION_CHANGE) {
                        val agentMode: ReplyBubbleAttributes = Gson().fromJson(
                            chatResponse.attachment?.attributes,
                            ReplyBubbleAttributes::class.java
                        )
                        handleReplyBubble(agentMode)
                    }

                } catch (e: JsonSyntaxException) { }
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
                connectWebSocket(messageId)
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

    private fun updateToolbar(tool: ToolbarAttributes?) {

        var profileImage = ""

        tool?.profileImage?.let {
            profileImage = it
        }

        if (view.isInDarkMode()) {
            tool?.profileImageDark?.let {
                profileImage = it
            }
        }

        view.updateToolbar(tool?.profileName,profileImage, tool?.badgeImage)

    }

    private fun handleReplyBubble(agentMode: ReplyBubbleAttributes) {
        if (agentMode!=null){
            if (agentMode.sessionChange.mode==MODE_AGENT){
                view.replyBubbleStateHandler(true)
            }else if (agentMode.sessionChange.mode==MODE_BOT){
                view.replyBubbleStateHandler(false)
            }
        }
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
        RxWebSocket.send(SendChatbotWebsocketParam.getReadMessage(messageId),
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
        data.addProperty("msg_id", messageId.convertMessageIdToLong())
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
                                       opponentId: String, isArticleEntry: Boolean, usedBy: String) {

        if (!isArticleEntry) {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamSendInvoice(
                    messageId,
                    invoiceLinkPojo, startTime, opponentId
                ), listInterceptor
            )
        } else {
            RxWebSocket.send(
                SendChatbotWebsocketParam.generateParamInvoiceSendByArticle(
                    messageId,
                    invoiceLinkPojo, startTime, opponentId, usedBy
                ), listInterceptor
            )
        }
    }

    override fun sendQuickReply(messageId: String, quickReply: QuickReplyViewModel,
                                startTime: String,
                                opponentId: String) {
        RxWebSocket.send(SendChatbotWebsocketParam.generateParamSendQuickReply(messageId,
                quickReply, startTime, opponentId), listInterceptor)
    }

    override fun sendQuickReplyInvoice(messageId: String, quickReply: QuickReplyViewModel,
                                startTime: String,
                                opponentId: String,event : String, usedBy: String) {
        RxWebSocket.send(
            SendChatbotWebsocketParam.generateParamSendQuickReplyEventArticle(
                messageId,
                quickReply, startTime, opponentId, event, usedBy
            ), listInterceptor
        )
    }

    override fun sendMessageWithApi(messageId: String, sendMessage: String, startTime: String) {
        //TODO
    }

    override fun sendMessageWithWebsocket(messageId: String, sendMessage: String,
                                          startTime: String, opponentId: String) {
        RxWebSocket.send(SendChatbotWebsocketParam.generateParamSendMessage(messageId, sendMessage,
                startTime, opponentId),
                listInterceptor)
    }


    override fun generateInvoice(
        invoiceLinkPojo: InvoiceLinkPojo, senderId: String
    ) : com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel {
        return com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel.Builder()
            .withInvoiceAttributesResponse(invoiceLinkPojo)
            .withFromUid(senderId)
            .withFrom(userSession.name)
            .withAttachmentType(AttachmentType.Companion.TYPE_INVOICE_SEND)
            .withReplyTime(SENDING_TEXT)
            .withStartTime(SendableUiModel.generateStartTime())
            .withIsRead(false)
            .withIsDummy(true)
            .withIsSender(true)
            .build()
    }

    override fun uploadImages(it: ImageUploadUiModel,
                              messageId: String,
                              opponentId: String,
                              onError: (Throwable, ImageUploadUiModel) -> Unit) {
        if (validateImageAttachment(it.imageUrl, MAX_FILE_SIZE)) {
            isUploading = true
            uploadImageUseCase.unsubscribe()

            val reqParam = HashMap<String, RequestBody>()
            val webService = "1".toRequestBody("text/plain".toMediaTypeOrNull())
            reqParam.put("web_service", createRequestBody("1"))
            reqParam.put(
                "id",
                createRequestBody(String.format("%s%s", userSession.userId, it.imageUrl))
            )
            val params = uploadImageUseCase.createRequestParam(
                it.imageUrl,
                "/upload/attachment",
                "fileToUpload\"; filename=\"image.jpg",
                reqParam
            )

            uploadImageUseCase.execute(params,
                object : Subscriber<ImageUploadDomainModel<ChatbotUploadImagePojo>>() {
                    override fun onNext(t: ImageUploadDomainModel<ChatbotUploadImagePojo>) {
                        t.dataResultImageUpload.data?.run {
                            sendUploadedImageToWebsocket(
                                ChatbotSendWebsocketParam
                                    .generateParamSendImage(
                                        messageId,
                                        this.picSrc,
                                        this.picObj,
                                        it.startTime,
                                        opponentId
                                    )
                            )
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

    override fun uploadImageSecureUpload(
            imageUploadViewModel: ImageUploadUiModel,
            messageId: String,
            opponentId: String,
            onErrorImageUpload: (Throwable, ImageUploadUiModel) -> Unit,
            path: String?,
            context: Context?
    ) {
        if (validateImageAttachment(imageUploadViewModel.imageUrl, MAX_FILE_SIZE_UPLOAD_SECURE)) {
            chatBotSecureImageUploadUseCase.setRequestParams(messageId, path ?: "")
            chatBotSecureImageUploadUseCase.execute(object : Subscriber<Map<Type?, RestResponse?>?>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    onErrorImageUpload(e, imageUploadViewModel)
                }

                override fun onNext(t: Map<Type?, RestResponse?>?) {
                    val token = object : TypeToken<UploadSecureResponse?>() {}.type
                    val restResponse = t?.get(token)
                    val uploadSecureResponse: UploadSecureResponse? = restResponse?.getData()
                    sendUploadedImageToWebsocket(
                            ChatbotSendWebsocketParam
                                    .generateParamUploadSecureSendImage(
                                            messageId,
                                            uploadSecureResponse?.uploadSecureData?.urlImage ?: "",
                                            imageUploadViewModel.startTime,
                                            opponentId,
                                            userSession.name)
                    )
                }

            })
        }

    }

    override fun createAttachInvoiceSingleViewModel(hashMap: Map<String, String>): AttachInvoiceSingleViewModel {
        return AttachInvoiceSingleViewModel(
            typeString = "",
            type = 0,
            code = hashMap[CODE] ?: "",
            createdTime = SendableUiModel.generateStartTime(),
            description = hashMap[DESCRIPTION] ?: "",
            url = hashMap[IMAGE_URL] ?: "",
            id = hashMap.get(ID)!!.toLongOrZero(),
            imageUrl = hashMap[IMAGE_URL] ?: "",
            status = hashMap[STATUS] ?: "",
            statusId = hashMap[STATUS_ID]!!.toIntOrZero(),
            title = hashMap[TITLE] ?: "",
            amount = hashMap[TOTAL_AMOUNT] ?: ""
        )
    }

    override fun getValuesForArticleEntry(uri: Uri): Map<String, String> {
        return mapOf(
            ARTICLE_ID to getQueryParam(uri, ARTICLE_ID),
            ARTICLE_TITLE to getQueryParam(uri, ARTICLE_TITLE),
            CODE to getQueryParam(uri, CODE),
            CREATE_TIME to getQueryParam(uri, CREATE_TIME),
            DESCRIPTION to getQueryParam(uri, DESCRIPTION),
            EVENT to getQueryParam(uri, EVENT),
            ID to getQueryParam(uri, ID),
            IMAGE_URL to getQueryParam(uri, IMAGE_URL),
            IS_ATTACHED to getQueryParam(uri, IS_ATTACHED),
            STATUS to getQueryParam(uri, STATUS),
            STATUS_COLOR to getQueryParam(uri, STATUS_COLOR),
            STATUS_ID to getQueryParam(uri, STATUS_ID),
            TITLE to getQueryParam(uri, TITLE),
            TOTAL_AMOUNT to getQueryParam(uri, TOTAL_AMOUNT),
            USED_BY to getQueryParam(uri, USED_BY)
        )

    }

    override fun sendMessage(
        messageId: String,
        sendMessage: String,
        startTime: String,
        opponentId: String,
        parentReply: ParentReply?,
        onSendingMessage: () -> Unit
    ) {
        if(isValidReply(sendMessage)) {
            onSendingMessage()
            if (parentReply == null) {
                RxWebSocket.send(
                    ChatbotSendWebsocketParam.generateParamSendMessage(
                        messageId, sendMessage,
                        startTime, opponentId
                    ),
                    listInterceptor
                )
            } else {
                RxWebSocket.send(
                    ChatbotSendWebsocketParam.generateParamSendMessageWithReplyBubble(
                        messageId, sendMessage, startTime, parentReply
                    ), listInterceptor
                )
            }
        }
    }

    private fun getQueryParam(uri: Uri, key: String): String {
        return uri.getQueryParameter(key).toBlankOrString()
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
        return content.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun validateImageAttachment(uri: String?, maxFileSize:Int): Boolean {

        if (uri == null) return false
        val file = File(uri)
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth

        val fileSize = (file.length() / DEFAULT_ONE_MEGABYTE).toString().toIntOrZero()

        return if (imageHeight < MINIMUM_HEIGHT || imageWidth < MINIMUM_WIDTH) {
            view.onUploadUndersizedImage()
            false
        } else if (fileSize >= maxFileSize) {
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
        sendChatRatingUseCase.unsubscribe()
        sendRatingReasonUseCase.unsubscribe()
        submitCsatRatingUseCase.unsubscribe()
        leaveQueueUseCase.unsubscribe()
        getTickerDataUseCase.unsubscribe()
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

    override fun checkForSession(messageId: String) {
        val params = getTopBotNewSessionUseCase.createRequestParams(messageId)
        launchCatchError(
            block = {
                val response = getTopBotNewSessionUseCase.getTobBotUserSession(params)
                val isNewSession = response.topBotGetNewSession.isNewSession
                val isTypingBlocked = response.topBotGetNewSession.isTypingBlocked
                handleNewSession(isNewSession)
                handleReplyBox(isTypingBlocked)
            },
            onError = {
                view.loadChatHistory()
                view.enableTyping()
            }
        )
    }

    private fun handleReplyBox(isTypingBlocked: Boolean) {
        if (isTypingBlocked) view.blockTyping() else view.enableTyping()
    }

    private fun handleNewSession(isNewSession: Boolean) {
        if (isNewSession) view.startNewSession() else view.loadChatHistory()
    }

    override fun checkUploadSecure(messageId: String, data: Intent) {
        val params = checkUploadSecureUseCase.createRequestParams(messageId)
        launchCatchError(
            block = {
                val response = checkUploadSecureUseCase.checkUploadSecure(params)
                val isSecureUpload = response.topbotUploadSecureAvailability.uploadSecureAvailabilityData.isUsingUploadSecure
                if (isSecureUpload) view.uploadUsingSecureUpload(data) else view.uploadUsingOldMechanism(data)
            },
            onError = {
                view.loadChatHistory()
                view.enableTyping()
            }
        )
    }

    override fun clearGetChatUseCase() {
        getExistingChatUseCase.reset()
    }

    override fun setBeforeReplyTime(createTime : String) {
        getExistingChatUseCase.updateMinReplyTime(createTime)
    }

    override fun getExistingChat(messageId: String,
                                 onError: (Throwable) -> Unit,
                                 onSuccessGetChat: (ChatroomViewModel, ChatReplies) -> Unit,
                                 onGetChatRatingListMessageError: (String) -> Unit) {

        if(messageId.isNotEmpty()) {
            launchCatchError(
                block = {
                    val response = getExistingChatUseCase.getFirstPageChat(messageId)
                    val mappedResponse =  getExistingChatMapper.map(response)
                    val chatReplies = response.chatReplies
                    val inputList = getChatRatingData(mappedResponse)
                    if (!inputList.list.isNullOrEmpty()) {
                        getChatRatingList(inputList,
                            onChatRatingListSuccess(mappedResponse,onSuccessGetChat,chatReplies, onGetChatRatingListMessageError)
                        )
                    } else {
                        onSuccessGetChat(mappedResponse,chatReplies)
                    }
                },
                onError = {
                    onError.invoke(it)
                }
            )
        }

    }

    fun getChatRatingData(mappedPojo: ChatroomViewModel): ChipGetChatRatingListInput {
        val input = ChipGetChatRatingListInput()
        for (message in mappedPojo.listChat) {
            if (message is HelpFullQuestionsViewModel) {
                input.list.add(ChipGetChatRatingListInput.ChatRating(ChatbotGetExistingChatMapper.Companion.TYPE_OPTION_LIST.toIntOrZero(),message.helpfulQuestion?.caseChatId ?: "" ))
            }else if (message is CsatOptionsViewModel) {
                input.list.add(ChipGetChatRatingListInput.ChatRating(ChatbotGetExistingChatMapper.Companion.TYPE_CSAT_OPTIONS.toIntOrZero(),message.csat?.caseChatId ?: "" ))
            }
        }
        return input
    }

    fun getChatRatingList(
        inputList: ChipGetChatRatingListInput,
        onSuccessGetRatingList: (ChipGetChatRatingListResponse.ChipGetChatRatingList?) -> Unit
    ) {
        val input = inputList
        launchCatchError(
            block = {
                val gqlResponse =  chipGetChatRatingListUseCase.getChatRatingList(chipGetChatRatingListUseCase.generateParam(input))
                val response = gqlResponse.getData<ChipGetChatRatingListResponse>(ChipGetChatRatingListResponse::class.java)

                onSuccessGetRatingList(response.chipGetChatRatingList)
            },
            onError = {
                onGetChatRatingListError(it)
            }
        )
    }

    private fun onChatRatingListSuccess(
        mappedPojo: ChatroomViewModel,
        onSuccessGetChat: (ChatroomViewModel, ChatReplies) -> Unit,
        chatReplies: ChatReplies,
        onGetChatRatingListMessageError: (String) -> Unit
    )
            : (ChipGetChatRatingListResponse.ChipGetChatRatingList?) -> Unit = { ratings ->
        updateMappedPojo(mappedPojo, ratings,onGetChatRatingListMessageError)
        onSuccessGetChat(mappedPojo,chatReplies)
    }

    private val onGetChatRatingListError: (Throwable) -> Unit = {
        it.printStackTrace()
    }

    fun updateMappedPojo(
        mappedPojo: ChatroomViewModel,
        ratings: ChipGetChatRatingListResponse.ChipGetChatRatingList?,
        onGetChatRatingListMessageError: (String) -> Unit
    ) {
        if (ratings?.ratingListData?.isSuccess == 1) {
            for (rate in ratings.ratingListData.list ?: listOf()) {
                val rateListMsgs = mappedPojo.listChat.filter { msg ->
                    when {
                        msg is HelpFullQuestionsViewModel && rate.attachmentType == ChatbotGetExistingChatMapper.Companion.TYPE_OPTION_LIST.toIntOrZero()
                        -> (msg.helpfulQuestion?.caseChatId == rate.caseChatID)
                        msg is CsatOptionsViewModel && rate.attachmentType == ChatbotGetExistingChatMapper.Companion.TYPE_CSAT_OPTIONS.toIntOrZero()
                        -> (msg.csat?.caseChatId == rate.caseChatID)
                        else -> false
                    }
                }
                rateListMsgs.forEach {
                    if (it is HelpFullQuestionsViewModel) {
                        it.isSubmited = rate.isSubmitted ?: true
                    }else if (it is CsatOptionsViewModel){
                        it.isSubmited = rate.isSubmitted ?: true
                    }
                }

            }
        } else if (!ratings?.messageError.isNullOrEmpty()) {
            onGetChatRatingListMessageError(ratings?.messageError?.get(0) ?: "")
        }

    }

    override fun getTopChat(
        messageId: String,
        onSuccessGetChat: (ChatroomViewModel, ChatReplies) -> Unit,
        onError: (Throwable) -> Unit,
        onGetChatRatingListMessageError: (String) -> Unit
    ) {
        launchCatchError(
            block = {
                val gqlResponse = getExistingChatUseCase.getTopChat(messageId)
                val chatReplies = gqlResponse.chatReplies
                val mappedResponse = getExistingChatMapper.map(gqlResponse)
                val inputList = getChatRatingData(mappedResponse)
                if (!inputList.list.isNullOrEmpty()) {
                    getChatRatingList(inputList,
                        onChatRatingListSuccess(mappedResponse,onSuccessGetChat,chatReplies, onGetChatRatingListMessageError)
                    )
                } else {
                    onSuccessGetChat(mappedResponse,chatReplies)
                }
            },
            onError = {
                onError.invoke(it)
            }
        )
    }

    override fun getBottomChat(
        messageId: String,
        onSuccessGetChat: (ChatroomViewModel, ChatReplies) -> Unit,
        onError: (Throwable) -> Unit,
        onGetChatRatingListMessageError: (String) -> Unit
    ) {
        launchCatchError(
            block = {
                val gqlResponse = getExistingChatUseCase.getBottomChat(messageId)
                val chatReplies = gqlResponse.chatReplies
                val mappedResponse = getExistingChatMapper.map(gqlResponse)
                val inputList = getChatRatingData(mappedResponse)
                if (!inputList.list.isNullOrEmpty()) {
                    getChatRatingList(inputList,
                        onChatRatingListSuccess(mappedResponse,onSuccessGetChat,chatReplies, onGetChatRatingListMessageError)
                    )
                } else {
                    onSuccessGetChat(mappedResponse,chatReplies)
                }
            },
            onError = {
                onError.invoke(it)
            }
        )
    }
}