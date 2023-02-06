package com.tokopedia.chatbot.view.presenter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.SESSION_CHANGE
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
import com.tokopedia.chatbot.ChatbotConstant.ImageUpload.MAX_FILE_SIZE_UPLOAD_SECURE
import com.tokopedia.chatbot.ChatbotConstant.ImageUpload.MINIMUM_HEIGHT
import com.tokopedia.chatbot.ChatbotConstant.ImageUpload.MINIMUM_WIDTH
import com.tokopedia.chatbot.ChatbotConstant.MODE_AGENT
import com.tokopedia.chatbot.ChatbotConstant.MODE_BOT
import com.tokopedia.chatbot.ChatbotConstant.NewRelic.KEY_CHATBOT_GET_CHATLIST_RATING
import com.tokopedia.chatbot.ChatbotConstant.NewRelic.KEY_CHATBOT_SECURE_UPLOAD_AVAILABILITY
import com.tokopedia.chatbot.ChatbotConstant.NewRelic.KEY_SECURE_UPLOAD
import com.tokopedia.chatbot.ChatbotConstant.ReplyBoxType.DYNAMIC_ATTACHMENT
import com.tokopedia.chatbot.ChatbotConstant.ReplyBoxType.REPLY_BOX_TOGGLE_VALUE
import com.tokopedia.chatbot.ChatbotConstant.ReplyBoxType.TYPE_BIG_REPLY_BOX
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleUiModel
import com.tokopedia.chatbot.data.network.ChatbotUrl
import com.tokopedia.chatbot.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.data.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.data.replybubble.ReplyBubbleAttributes
import com.tokopedia.chatbot.data.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.data.uploadEligibility.ChatbotUploadVideoEligibilityResponse
import com.tokopedia.chatbot.data.uploadsecure.UploadSecureResponse
import com.tokopedia.chatbot.domain.mapper.ChatBotWebSocketMessageMapper
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.chatbot.domain.pojo.csatRating.csatInput.InputItem
import com.tokopedia.chatbot.domain.pojo.csatRating.csatResponse.SubmitCsatGqlResponse
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.domain.pojo.livechatdivider.LiveChatDividerAttributes
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.domain.pojo.replyBox.BigReplyBoxAttribute
import com.tokopedia.chatbot.domain.pojo.replyBox.DynamicAttachment
import com.tokopedia.chatbot.domain.pojo.replyBox.ReplyBoxAttribute
import com.tokopedia.chatbot.domain.pojo.replyBox.SmallReplyBoxAttribute
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.chatbot.domain.pojo.submitoption.SubmitOptionInput
import com.tokopedia.chatbot.domain.socket.ChatbotSendableWebSocketParam
import com.tokopedia.chatbot.domain.usecase.ChatBotSecureImageUploadUseCase
import com.tokopedia.chatbot.domain.usecase.ChatbotUploadVideoEligibilityUseCase
import com.tokopedia.chatbot.domain.usecase.CheckUploadSecureUseCase
import com.tokopedia.chatbot.domain.usecase.ChipGetChatRatingListUseCase
import com.tokopedia.chatbot.domain.usecase.ChipSubmitChatCsatUseCase
import com.tokopedia.chatbot.domain.usecase.ChipSubmitHelpfulQuestionsUseCase
import com.tokopedia.chatbot.domain.usecase.GetExistingChatUseCase
import com.tokopedia.chatbot.domain.usecase.GetResolutionLinkUseCase
import com.tokopedia.chatbot.domain.usecase.GetTickerDataUseCase
import com.tokopedia.chatbot.domain.usecase.GetTopBotNewSessionUseCase
import com.tokopedia.chatbot.domain.usecase.SendChatRatingUseCase
import com.tokopedia.chatbot.domain.usecase.SubmitCsatRatingUseCase
import com.tokopedia.chatbot.util.ChatbotNewRelicLogger
import com.tokopedia.chatbot.util.ChatbotVideoUploadResult
import com.tokopedia.chatbot.util.VideoUploadData
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.CHAT_DIVIDER
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.OPEN_CSAT
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.QUERY_SOURCE_TYPE
import com.tokopedia.chatbot.view.presenter.ChatbotPresenter.companion.UPDATE_TOOLBAR
import com.tokopedia.chatbot.view.util.Attachment34RenderType
import com.tokopedia.chatbot.view.util.CheckDynamicAttachmentValidity
import com.tokopedia.chatbot.websocket.ChatWebSocketResponse
import com.tokopedia.chatbot.websocket.ChatbotWebSocket
import com.tokopedia.chatbot.websocket.ChatbotWebSocketAction
import com.tokopedia.chatbot.websocket.ChatbotWebSocketImpl
import com.tokopedia.chatbot.websocket.ChatbotWebSocketStateHandler
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import rx.Subscriber
import timber.log.Timber
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by nisie on 05/12/18.
 */

typealias MediaUploadJobMap = Map<String, Job>
typealias MediaUploadResultMap = Map<String, ChatbotVideoUploadResult>
class ChatbotPresenter @Inject constructor(
    var getExistingChatUseCase: GetExistingChatUseCase,
    override var userSession: UserSessionInterface,
    private var chatBotWebSocketMessageMapper: ChatBotWebSocketMessageMapper,
    private val tkpdAuthInterceptor: TkpdAuthInterceptor,
    private val fingerprintInterceptor: FingerprintInterceptor,
    private val sendChatRatingUseCase: SendChatRatingUseCase,
    private val submitCsatRatingUseCase: SubmitCsatRatingUseCase,
    private val getTickerDataUseCase: GetTickerDataUseCase,
    private val chipSubmitHelpfulQuestionsUseCase: ChipSubmitHelpfulQuestionsUseCase,
    private val chipGetChatRatingListUseCase: ChipGetChatRatingListUseCase,
    private val chipSubmitChatCsatUseCase: ChipSubmitChatCsatUseCase,
    private val getResolutionLinkUseCase: GetResolutionLinkUseCase,
    private val getTopBotNewSessionUseCase: GetTopBotNewSessionUseCase,
    private val checkUploadSecureUseCase: CheckUploadSecureUseCase,
    private val chatBotSecureImageUploadUseCase: ChatBotSecureImageUploadUseCase,
    private val getExistingChatMapper: ChatbotGetExistingChatMapper,
    private val uploaderUseCase: UploaderUseCase,
    private val chatbotVideoUploadVideoEligibilityUseCase: ChatbotUploadVideoEligibilityUseCase,
    private val chatbotWebSocket: ChatbotWebSocket,
    private val chatbotWebSocketStateHandler: ChatbotWebSocketStateHandler,
    private val dispatcher: CoroutineDispatchers

) : BaseChatPresenter<ChatbotContract.View>(userSession, chatBotWebSocketMessageMapper), ChatbotContract.Presenter, CoroutineScope {

    object companion {
        const val OPEN_CSAT = "13"
        const val UPDATE_TOOLBAR = "14"
        const val CHAT_DIVIDER = "15"
        const val QUERY_SOURCE_TYPE = "Apps"
    }

    override fun clearText() {
        view.clearChatText()
    }

    override fun isUploading(): Boolean {
        return isUploading
    }

    private var isUploading: Boolean = false
    private var listInterceptor: ArrayList<Interceptor> =
        arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)
    private var chatResponse: ChatSocketPojo? = null
    private val job = SupervisorJob()
    private var autoRetryJob: Job? = null
    var socketJob: Job? = null

    private var mediaUploadJobs = MutableStateFlow<MediaUploadJobMap>(mapOf())
    var mediaUploadResults = MutableStateFlow<MediaUploadResultMap>(mapOf())
    private val shouldResetFailedUploadStatus = MutableStateFlow(false)
    val mediaUris = MutableStateFlow<List<VideoUploadData>>(emptyList())

    init {
        observeMediaUrisForUpload()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun connectWebSocket(messageId: String) {
        socketJob?.cancel()

        val webSocketUrl = ChatbotUrl.getPathWebsocket(userSession.deviceId, userSession.userId)

        socketJob = launchCatchError(
            dispatcher.main,
            block = {
                if (!isActive) return@launchCatchError

                chatbotWebSocket.connect(webSocketUrl)

                chatbotWebSocket
                    .getDataFromSocketAsFlow()
                    .collect {
                        handleWebSocketResponse(it, messageId)
                    }
            },
            onError = {
                ChatbotNewRelicLogger.logNewRelicForSocket(
                    it
                )
            }
        )
    }

    private fun handleWebSocketResponse(socketResponse: ChatbotWebSocketAction, messageId: String) {
        when (socketResponse) {
            is ChatbotWebSocketAction.SocketOpened -> {
                handleSocketOpen(messageId)
            }
            is ChatbotWebSocketAction.NewMessage -> {
                handleNewMessage(socketResponse.message, messageId)
            }
            is ChatbotWebSocketAction.Failure -> {
                handleSocketFailure(messageId)
            }
            is ChatbotWebSocketAction.Closed -> {
                handleSocketClosed(socketResponse.code, messageId)
            }
        }
    }

    private fun handleSocketOpen(messageId: String) {
        networkMode = MODE_WEBSOCKET
        sendReadEventWebSocket(messageId)
        view.showErrorWebSocket(false)
        view.sendInvoiceForArticle()
        chatbotWebSocketStateHandler.retrySucceed()
    }

    private fun handleNewMessage(message: ChatWebSocketResponse, messageId: String) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Timber.d("Socket Message: $message")
        }
        handleAttachmentTypes(message, messageId)
    }

    private fun handleSocketFailure(messageId: String) {
        view.showErrorWebSocket(true)
        retryConnectToWebSocket(messageId)
    }

    private fun handleSocketClosed(code: Int, messageId: String) {
        networkMode = MODE_API
        if (code != ChatbotWebSocketImpl.CODE_NORMAL_CLOSURE) {
            retryConnectToWebSocket(messageId)
        }
    }

    private fun sendReadEventWebSocket(messageId: String) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.getReadMessageWebSocket(messageId),
            listInterceptor
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun retryConnectToWebSocket(messageId: String) {
        chatbotWebSocket.close()
        networkMode = MODE_WEBSOCKET
        autoRetryJob = launchCatchError(
            dispatcher.io,
            block = {
                chatbotWebSocketStateHandler.scheduleForRetry {
                    withContext(dispatcher.main) {
                        connectWebSocket(messageId)
                    }
                }
            },
            onError = {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    Timber.d("Socket Reconnecting")
                }
                ChatbotNewRelicLogger.logNewRelicForSocket(
                    it
                )
            }
        )
    }

    private fun mappingSocketEvent(webSocketResponse: ChatWebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo =
            Gson().fromJson(webSocketResponse.jsonObject, ChatSocketPojo::class.java)

        when (webSocketResponse.code) {
            EVENT_TOPCHAT_TYPING -> view.onReceiveStartTypingEvent()
            EVENT_TOPCHAT_END_TYPING -> view.onReceiveStopTypingEvent()
            EVENT_TOPCHAT_READ_MESSAGE -> view.onReceiveReadEvent()
            EVENT_TOPCHAT_REPLY_MESSAGE -> {
                val attachmentType = chatResponse?.attachment?.type
                if (attachmentType == SESSION_CHANGE ||
                    attachmentType == UPDATE_TOOLBAR ||
                    attachmentType == DYNAMIC_ATTACHMENT
                ) {
                    return
                }
                view.onReceiveMessageEvent(mapToVisitable(pojo))
                sendReadEventWebSocket(messageId)
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun handleAttachmentTypes(webSocketResponse: ChatWebSocketResponse, messageId: String) {
        try {
            val pojo: ChatSocketPojo =
                Gson().fromJson(webSocketResponse.jsonObject, ChatSocketPojo::class.java)
            if (pojo.msgId != messageId) return
            chatResponse = pojo

            mappingSocketEvent(webSocketResponse, messageId)

            val attachmentType = chatResponse?.attachment?.type
            if (attachmentType != null) {
                when (attachmentType) {
                    OPEN_CSAT -> handleOpenCsatAttachment(webSocketResponse)
                    UPDATE_TOOLBAR -> handleUpdateToolbarAttachment()
                    CHAT_DIVIDER -> handleChatDividerAttachment()
                    SESSION_CHANGE -> handleSessionChangeAttachment()
                    DYNAMIC_ATTACHMENT -> handleDynamicAttachment34(pojo)
                }
            }
        } catch (e: JsonSyntaxException) {
            ChatbotNewRelicLogger.logNewRelicForSocket(
                e
            )
        }
    }

    private fun handleOpenCsatAttachment(webSocketResponse: ChatWebSocketResponse) {
        val csatResponse: WebSocketCsatResponse = Gson().fromJson(
            webSocketResponse.jsonObject,
            WebSocketCsatResponse::class.java
        )
        view.openCsat(csatResponse)
    }

    private fun handleUpdateToolbarAttachment() {
        val tool =
            Gson().fromJson(chatResponse?.attachment?.attributes, ToolbarAttributes::class.java)
        view.updateToolbar(tool.profileName, tool.profileImage, tool.badgeImage)
    }

    private fun handleChatDividerAttachment() {
        val liveChatDividerAttribute = Gson().fromJson(
            chatResponse?.attachment?.attributes,
            LiveChatDividerAttributes::class.java
        )
        val model = ChatSepratorUiModel(
            sepratorMessage = liveChatDividerAttribute?.divider?.label,
            dividerTiemstamp = chatResponse?.message?.timeStampUnixNano ?: ""
        )
        view.onReceiveChatSepratorEvent(model, getLiveChatQuickReply())
    }

    private fun handleSessionChangeAttachment() {
        val agentMode: ReplyBubbleAttributes = Gson().fromJson(
            chatResponse?.attachment?.attributes,
            ReplyBubbleAttributes::class.java
        )
        handleSessionChange(agentMode)
    }

    private fun handleSessionChange(agentMode: ReplyBubbleAttributes) {
        if (agentMode.sessionChange.mode == MODE_AGENT) {
            view.sessionChangeStateHandler(true)
        } else if (agentMode.sessionChange.mode == MODE_BOT) {
            view.sessionChangeStateHandler(false)
        }
    }

    private fun getLiveChatQuickReply(): List<QuickReplyUiModel> {
        val quickReplyListPojo = GsonBuilder().create()
            .fromJson(
                chatResponse?.attachment?.attributes,
                QuickReplyAttachmentAttributes::class.java
            )
        val list = ArrayList<QuickReplyUiModel>()
        if (quickReplyListPojo != null && !quickReplyListPojo.quickReplies.isEmpty()) {
            for (pojo in quickReplyListPojo.quickReplies) {
                if (!TextUtils.isEmpty(pojo.text)) {
                    list.add(QuickReplyUiModel(pojo.text, pojo.value, pojo.action))
                }
            }
        }
        return list
    }

    @VisibleForTesting
    fun handleDynamicAttachment34(pojo: ChatSocketPojo) {
        val dynamicAttachmentContents =
            Gson().fromJson(pojo.attachment?.attributes, DynamicAttachment::class.java)

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        if (Attachment34RenderType.mapTypeToDeviceType(replyBoxAttribute?.renderTarget)
            == Attachment34RenderType.RenderAttachment34
        ) {
            when (replyBoxAttribute?.contentCode) {
                TYPE_BIG_REPLY_BOX -> {
                    convertToBigReplyBoxData(replyBoxAttribute.dynamicContent)
                }
                REPLY_BOX_TOGGLE_VALUE -> {
                    convertToSmallReplyBoxData(replyBoxAttribute.dynamicContent)
                }
                else -> {
                    // TODO need to show fallback message
                    mapToVisitable(pojo)
                }
            }
        }
    }

    private fun convertToBigReplyBoxData(dynamicContent: String?) {
        if (dynamicContent == null) {
            return
        }
        val bigReplyBoxContent = Gson().fromJson(
            dynamicContent,
            BigReplyBoxAttribute::class.java
        )
        handleBigReplyBoxWS(bigReplyBoxContent)
    }

    private fun convertToSmallReplyBoxData(dynamicContent: String?) {
        if (dynamicContent == null) {
            return
        }
        val smallReplyBoxContent = Gson().fromJson(
            dynamicContent,
            SmallReplyBoxAttribute::class.java
        )
        handleSmallReplyBoxWS(smallReplyBoxContent)
    }

    private fun handleBigReplyBoxWS(bigReplyBoxContent: BigReplyBoxAttribute) {
        if (bigReplyBoxContent.isActive) {
            view.setBigReplyBoxTitle(bigReplyBoxContent.title, bigReplyBoxContent.placeholder)
        }
    }

    private fun handleSmallReplyBoxWS(smallReplyBoxContent: SmallReplyBoxAttribute) {
        if (smallReplyBoxContent.isHidden) {
            view.hideReplyBox()
        } else {
            view.enableTyping()
        }
    }

    fun validateHistoryForAttachment34(replyBoxAttribute: ReplyBoxAttribute?): Boolean {
        if (replyBoxAttribute == null) {
            return false
        }

        if (CheckDynamicAttachmentValidity.checkValidity(replyBoxAttribute.contentCode)) {
            when (replyBoxAttribute.contentCode) {
                TYPE_BIG_REPLY_BOX -> {
                    convertToBigReplyBoxData(replyBoxAttribute.dynamicContent)
                    return true
                }
                REPLY_BOX_TOGGLE_VALUE -> {
                    convertToSmallReplyBoxData(replyBoxAttribute.dynamicContent)
                    return true
                }
            }
        }
        return false
    }

    override fun showErrorSnackbar(stringId: Int) {
        view.showSnackbarError(stringId)
    }

    override fun sendReadEvent(messageId: String) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.getReadMessage(messageId),
            listInterceptor
        )
    }

    override fun sendRating(messageId: String, rating: Int, element: ChatRatingUiModel) {
        sendChatRatingUseCase.sendChatRating(
            ::onSuccessSendRating,
            ::onFailureSendRating,
            messageId,
            rating,
            element
        )
    }

    private fun onFailureSendRating(throwable: Throwable, messageId: String) {
        view.onError(throwable)
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_SEND_RATING,
            throwable
        )
    }

    private fun onSuccessSendRating(pojo: SendRatingPojo, rating: Int, element: ChatRatingUiModel) {
        view.onSuccessSendRating(pojo, rating, element)
    }

    override fun sendActionBubble(
        messageId: String,
        selected: ChatActionBubbleUiModel,
        startTime: String,
        opponentId: String
    ) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.generateParamSendBubbleAction(
                messageId,
                selected,
                startTime,
                opponentId
            ),
            listInterceptor
        )
    }

    override fun destroyWebSocket() {
        socketJob?.cancel()
        autoRetryJob?.cancel()
        chatbotWebSocket.close()
    }

    override fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return chatBotWebSocketMessageMapper.map(pojo)
    }

    override fun sendInvoiceAttachment(
        messageId: String,
        invoiceLinkPojo: InvoiceLinkPojo,
        startTime: String,
        opponentId: String,
        isArticleEntry: Boolean,
        usedBy: String
    ) {
        if (!isArticleEntry) {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamSendInvoice(
                    messageId,
                    invoiceLinkPojo,
                    startTime,
                    opponentId
                ),
                listInterceptor
            )
        } else {
            chatbotWebSocket.send(
                ChatbotSendableWebSocketParam.generateParamInvoiceSendByArticle(
                    messageId,
                    invoiceLinkPojo,
                    startTime,
                    usedBy
                ),
                listInterceptor
            )
        }
    }

    override fun sendQuickReply(
        messageId: String,
        quickReply: QuickReplyUiModel,
        startTime: String,
        opponentId: String
    ) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.generateParamSendQuickReply(
                messageId,
                quickReply,
                startTime,
                opponentId
            ),
            listInterceptor
        )
    }

    override fun sendQuickReplyInvoice(
        messageId: String,
        quickReply: QuickReplyUiModel,
        startTime: String,
        opponentId: String,
        event: String,
        usedBy: String
    ) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.generateParamSendQuickReplyEventArticle(
                messageId,
                quickReply,
                startTime,
                event,
                usedBy
            ),
            listInterceptor
        )
    }

    override fun sendMessageWithApi(messageId: String, sendMessage: String, startTime: String) {
        // TODO
    }

    override fun sendMessageWithWebsocket(
        messageId: String,
        sendMessage: String,
        startTime: String,
        opponentId: String
    ) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.generateParamSendMessage(
                messageId,
                sendMessage,
                startTime,
                opponentId
            ),
            listInterceptor
        )
    }

    override fun generateInvoice(
        invoiceLinkPojo: InvoiceLinkPojo,
        senderId: String
    ): com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel {
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
                    ChatbotNewRelicLogger.logNewRelic(
                        false,
                        messageId,
                        KEY_SECURE_UPLOAD,
                        e
                    )
                    onErrorImageUpload(e, imageUploadViewModel)
                }

                override fun onNext(t: Map<Type?, RestResponse?>?) {
                    val token = object : TypeToken<UploadSecureResponse?>() {}.type
                    val restResponse = t?.get(token)
                    val uploadSecureResponse: UploadSecureResponse? = restResponse?.getData()
                    sendUploadedImageToWebsocket(
                        ChatbotSendableWebSocketParam
                            .generateParamUploadSecureSendImage(
                                messageId,
                                uploadSecureResponse?.uploadSecureData?.urlImage ?: "",
                                imageUploadViewModel.startTime,
                                userSession.name
                            )
                    )
                }
            })
        }
    }

    override fun createAttachInvoiceSingleViewModel(hashMap: Map<String, String>): AttachInvoiceSingleUiModel {
        return AttachInvoiceSingleUiModel(
            typeString = "",
            type = 0,
            code = hashMap[CODE] ?: "",
            createdTime = hashMap[CREATE_TIME] ?: "",
            description = hashMap[DESCRIPTION] ?: "",
            url = hashMap[IMAGE_URL] ?: "",
            id = hashMap.get(ID).toLongOrZero(),
            imageUrl = hashMap[IMAGE_URL] ?: "",
            status = hashMap[STATUS] ?: "",
            statusId = hashMap[STATUS_ID].toIntOrZero(),
            title = hashMap[TITLE] ?: "",
            amount = hashMap[TOTAL_AMOUNT] ?: "",
            color = hashMap[STATUS_COLOR] ?: ""
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
        if (isValidReply(sendMessage)) {
            onSendingMessage()
            if (parentReply == null) {
                chatbotWebSocket.send(
                    ChatbotSendableWebSocketParam.generateParamSendMessage(
                        messageId,
                        sendMessage,
                        startTime,
                        opponentId
                    ),
                    listInterceptor
                )
            } else {
                chatbotWebSocket.send(
                    ChatbotSendableWebSocketParam.generateParamSendMessageWithReplyBubble(
                        messageId,
                        sendMessage,
                        startTime,
                        parentReply
                    ),
                    listInterceptor
                )
            }
        }
    }

    private fun getQueryParam(uri: Uri, key: String): String {
        return uri.getQueryParameter(key).toBlankOrString()
    }

    override fun cancelImageUpload() {
        chatBotSecureImageUploadUseCase.unsubscribe()
    }

    fun sendUploadedImageToWebsocket(json: JsonObject) {
        val interceptors = ArrayList<Interceptor>()
        interceptors.add(tkpdAuthInterceptor)
        interceptors.add(fingerprintInterceptor)
        chatbotWebSocket.send(json, interceptors)
    }

    private fun validateImageAttachment(uri: String?, maxFileSize: Int): Boolean {
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

    override fun submitCsatRating(messageId: String, inputItem: InputItem) {
        submitCsatRatingUseCase.cancelJobs()

        submitCsatRatingUseCase.submitCsatRating(
            ::onSuccessSubmitCsatRating,
            ::onErrorSubmitCsatRating,
            inputItem,
            messageId
        )
    }

    fun onSuccessSubmitCsatRating(submitCsatGqlResponse: SubmitCsatGqlResponse) {
        view.onSuccessSubmitCsatRating(submitCsatGqlResponse.submitRatingCSAT?.data?.message.toString())
    }

    private fun onErrorSubmitCsatRating(throwable: Throwable, messageId: String) {
        view.onError(throwable)
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_CSAT_RATING,
            throwable
        )
    }

    override fun hitGqlforOptionList(messageId: String, selectedValue: Int, model: HelpFullQuestionsUiModel?) {
        val input = generateInput(selectedValue, model)
        chipSubmitHelpfulQuestionsUseCase.cancelJobs()
        chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(
            ::onErrorOptionList,
            input,
            messageId
        )
    }

    fun onSubmitError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    private fun onErrorOptionList(throwable: Throwable, messageId: String) {
        onSubmitError(throwable)
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_SUBMIT_HELPFULL_QUESTION,
            throwable
        )
    }

    private fun generateInput(selectedValue: Int, model: HelpFullQuestionsUiModel?): SubmitOptionInput {
        val input = SubmitOptionInput()
        with(input) {
            caseChatID = model?.helpfulQuestion?.caseChatId ?: ""
            caseID = model?.helpfulQuestion?.caseId ?: ""
            messageID = model?.messageId ?: ""
            source = QUERY_SOURCE_TYPE
            value = selectedValue
        }
        return input
    }

    override fun submitChatCsat(messageId: String, input: ChipSubmitChatCsatInput) {
        chipSubmitChatCsatUseCase.cancelJobs()
        chipSubmitChatCsatUseCase.chipSubmitChatCsat(
            ::onSuccessSubmitChatCsat,
            ::onFailureSubmitChatCsat,
            input,
            messageId
        )
    }

    private fun onSuccessSubmitChatCsat(chipSubmitChatCsatResponse: ChipSubmitChatCsatResponse) {
        view.onSuccessSubmitChatCsat(chipSubmitChatCsatResponse.chipSubmitChatCSAT?.csatSubmitData?.toasterMessage ?: "")
    }

    private fun onFailureSubmitChatCsat(throwable: Throwable, messageId: String) {
        view.onError(throwable)
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_SUBMIT_CHAT_CSAT,
            throwable
        )
    }

    override fun checkLinkForRedirection(
        messageId: String,
        invoiceRefNum: String,
        onGetSuccessResponse: (String) -> Unit,
        setStickyButtonStatus: (Boolean) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val params = getResolutionLinkUseCase.createRequestParams(invoiceRefNum)
        launchCatchError(
            block = {
                val orderList =
                    getResolutionLinkUseCase
                        .getResoLinkResponse(params)
                        .getResolutionLink?.resolutionLinkData?.orderList?.firstOrNull()
                if (orderList?.resoList?.isNotEmpty() == true) {
                    setStickyButtonStatus(true)
                } else {
                    setStickyButtonStatus(false)
                }
                val link = orderList?.dynamicLink ?: ""
                onGetSuccessResponse(getEnvResoLink(link))
            },
            onError = {
                onError(it)
                ChatbotNewRelicLogger.logNewRelic(
                    false,
                    messageId,
                    ChatbotConstant.NewRelic.KEY_CHATBOT_GET_LINK_FOR_REDIRECTION,
                    it
                )
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
        job.cancel()
        chatbotVideoUploadVideoEligibilityUseCase.cancelJobs()
        super.detachView()
    }

    override fun showTickerData(messageId: String) {
        getTickerDataUseCase.cancelJobs()
        getTickerDataUseCase.getTickerData(
            ::onSuccessGetTickerData,
            ::onErrorGetTickerData,
            messageId
        )
    }

    private fun onErrorGetTickerData(throwable: Throwable, messageId: String) {
        view.onError(throwable)
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_TICKER,
            throwable
        )
    }

    private fun onSuccessGetTickerData(tickerData: TickerDataResponse) {
        tickerData.chipGetActiveTickerV4?.data?.let { tickerData ->
            view.onSuccessGetTickerData(tickerData)
        }
    }

    override fun getActionBubbleforNoTrasaction(): ChatActionBubbleUiModel {
        val text = view.context?.getString(R.string.chatbot_text_for_no_transaction_found) ?: ""
        val value = view.context?.getString(R.string.chatbot_text_for_no_transaction_found) ?: ""
        val action = view.context?.getString(R.string.chatbot_action_text_for_no_transaction_found) ?: ""
        return ChatActionBubbleUiModel(text, value, action)
    }

    override fun checkForSession(messageId: String) {
        getTopBotNewSessionUseCase.cancelJobs()
        getTopBotNewSessionUseCase.getTopBotUserSession(
            ::getTopBotNewSessionSuccess,
            ::getTopBotNewSessionFailure,
            messageId
        )
    }

    private fun getTopBotNewSessionFailure(throwable: Throwable, messageId: String) {
        view.loadChatHistory()
        view.enableTyping()
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_NEW_SESSION,
            throwable
        )
    }

    private fun getTopBotNewSessionSuccess(topBotNewSessionResponse: TopBotNewSessionResponse) {
        val isNewSession = topBotNewSessionResponse.topBotGetNewSession.isNewSession
        val isTypingBlocked = topBotNewSessionResponse.topBotGetNewSession.isTypingBlocked
        handleNewSession(isNewSession)
        handleReplyBox(isTypingBlocked)
    }

    private fun handleReplyBox(isTypingBlocked: Boolean) {
        if (isTypingBlocked) view.hideReplyBox() else view.enableTyping()
    }

    private fun handleNewSession(isNewSession: Boolean) {
        if (isNewSession) view.startNewSession() else view.loadChatHistory()
    }

    override fun checkUploadSecure(messageId: String, data: Intent) {
        val params = checkUploadSecureUseCase.createRequestParams(messageId)
        launchCatchError(
            block = {
                checkUploadSecureUseCase.checkUploadSecure(params)
                view.uploadUsingSecureUpload(data)
            },
            onError = {
                ChatbotNewRelicLogger.logNewRelic(
                    false,
                    messageId,
                    KEY_CHATBOT_SECURE_UPLOAD_AVAILABILITY,
                    it
                )
                view.loadChatHistory()
                view.enableTyping()
            }
        )
    }

    fun filterMediaUploadJobs(originalPaths: List<String>) {
        mediaUploadJobs.value.filterKeys(originalPaths::contains)
    }

    fun updateMediaUris(paths: List<VideoUploadData>) {
        mediaUris.value = paths
    }

    private fun updateMediaUploadJobs(uri: String, newUploadJob: Job) {
        mediaUploadJobs.value = mutableMapOf<String, Job>().apply {
            putAll(mediaUploadJobs.value)
            put(uri, newUploadJob)
        }
    }

    private fun observeMediaUrisForUpload() {
        launch {
            combine(
                shouldResetFailedUploadStatus,
                mediaUris
            ) { shouldResetFailedUploadStatus, uris ->
                shouldResetFailedUploadStatus to uris
            }.collectLatest(::tryUploadMedia)
        }
    }

    private suspend fun tryUploadMedia(data: Pair<Boolean, List<VideoUploadData>>) {
        val (shouldResetFailedUploadStatus, videoData) = data
        if (shouldResetFailedUploadStatus) {
            mediaUploadResults.value = mediaUploadResults.value.filterValues { uploadResult ->
                uploadResult is ChatbotVideoUploadResult.Success
            }
            this@ChatbotPresenter.shouldResetFailedUploadStatus.value = false
        } else {
            videoData.forEach {
                val currentUri = it.videoPath ?: ""
                val hasActiveUploadJob = mediaUploadJobs.value[currentUri]?.isActive == true
                val needToStartNewJob = !hasActiveUploadJob
                if (needToStartNewJob) {
                    updateMediaUploadJobs(
                        currentUri,
                        startNewUploadMediaJob(
                            currentUri,
                            it.messageId,
                            it.startTime
                        )
                    )
                    mediaUploadJobs.value.values.joinAll()
                }
            }
        }
    }

    fun startNewUploadMediaJob(
        uri: String,
        messageId: String,
        startTime: String
    ): Job {
        return launchCatchError(block = {
            val filePath = File(uri)
            val params = uploaderUseCase.createParams(
                sourceId = ChatbotConstant.VideoUpload.SOURCE_ID_FOR_VIDEO_UPLOAD,
                filePath = filePath,
                withTranscode = false
            )
            val uploadMediaResult = uploaderUseCase(params).let {
                when (it) {
                    is UploadResult.Success -> {
                        sendVideoAttachment(it.videoUrl, startTime, messageId)
                        ChatbotVideoUploadResult.Success(it.uploadId, it.videoUrl)
                    }
                    is UploadResult.Error -> {
                        ChatbotVideoUploadResult.Error(it.message)
                    }
                }
            }
            updateMediaUploadResults(uri, uploadMediaResult)
        }, onError = {
                updateMediaUploadResults(uri, ChatbotVideoUploadResult.Error(it.message.orEmpty()))
            })
    }

    fun updateMediaUploadResults(
        uri: String,
        uploadMediaResult: ChatbotVideoUploadResult
    ) {
        mediaUploadResults.value = mutableMapOf<String, ChatbotVideoUploadResult>().apply {
            putAll(mediaUploadResults.value)
            put(uri, uploadMediaResult)
        }
    }

    override fun sendVideoAttachment(filePath: String, startTime: String, messageId: String) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.generateParamSendVideoAttachment(
                filePath,
                startTime,
                messageId
            ),
            listInterceptor
        )
    }

    override fun cancelVideoUpload(file: String, sourceId: String, onErrorVideoUpload: (Throwable) -> Unit) {
        launchCatchError(
            block = {
                uploaderUseCase.abortUpload(
                    filePath = file,
                    sourceId = sourceId
                )
                mediaUploadJobs.value.get(file)?.cancel()
            },
            onError = {
                onErrorVideoUpload(it)
            }
        )
    }

    override fun checkUploadVideoEligibility(msgId: String) {
        chatbotVideoUploadVideoEligibilityUseCase.cancelJobs()
        chatbotVideoUploadVideoEligibilityUseCase.getVideoUploadEligibility(
            ::onSuccessVideoUploadEligibility,
            ::onFailureVideoUploadEligibility,
            msgId
        )
    }

    private fun onSuccessVideoUploadEligibility(response: ChatbotUploadVideoEligibilityResponse) {
        view.videoUploadEligibilityHandler(response.topbotUploadVideoEligibility.dataVideoEligibility.isEligible)
    }

    private fun onFailureVideoUploadEligibility(throwable: Throwable) {
        // Add new Relic Here
    }

    override fun clearGetChatUseCase() {
        getExistingChatUseCase.reset()
    }

    override fun setBeforeReplyTime(createTime: String) {
        getExistingChatUseCase.updateMinReplyTime(createTime)
    }

    override fun getExistingChat(
        messageId: String,
        onError: (Throwable) -> Unit,
        onSuccessGetChat: (ChatroomViewModel, ChatReplies) -> Unit,
        onGetChatRatingListMessageError: (String) -> Unit
    ) {
        if (messageId.isNotEmpty()) {
            launchCatchError(
                block = {
                    val response = getExistingChatUseCase.getFirstPageChat(messageId)
                    val mappedResponse = getExistingChatMapper.map(response)
                    val chatReplies = response.chatReplies
                    val inputList = getChatRatingData(mappedResponse)
                    if (!inputList.list.isNullOrEmpty()) {
                        getChatRatingList(
                            inputList,
                            messageId,
                            onChatRatingListSuccess(mappedResponse, onSuccessGetChat, chatReplies, onGetChatRatingListMessageError)
                        )
                    } else {
                        onSuccessGetChat(mappedResponse, chatReplies)
                    }
                },
                onError = {
                    onError.invoke(it)

                    ChatbotNewRelicLogger.logNewRelic(
                        false,
                        messageId,
                        ChatbotConstant.NewRelic.KEY_CHATBOT_GET_EXISTING_CHAT_FIRST_TIME,
                        it
                    )
                }
            )
        }
    }

    fun getChatRatingData(mappedPojo: ChatroomViewModel): ChipGetChatRatingListInput {
        val input = ChipGetChatRatingListInput()
        for (message in mappedPojo.listChat) {
            if (message is HelpFullQuestionsUiModel) {
                input.list.add(
                    ChipGetChatRatingListInput.ChatRating(
                        ChatbotGetExistingChatMapper.Companion.TYPE_OPTION_LIST.toIntOrZero(),
                        message.helpfulQuestion?.caseChatId ?: ""
                    )
                )
            } else if (message is CsatOptionsUiModel) {
                input.list.add(
                    ChipGetChatRatingListInput.ChatRating(
                        ChatbotGetExistingChatMapper.Companion.TYPE_CSAT_OPTIONS.toIntOrZero(),
                        message.csat?.caseChatId ?: ""
                    )
                )
            }
        }
        return input
    }

    fun getChatRatingList(
        inputList: ChipGetChatRatingListInput,
        messageId: String,
        onSuccessGetRatingList: (ChipGetChatRatingListResponse.ChipGetChatRatingList?) -> Unit
    ) {
        val input = inputList
        launchCatchError(
            block = {
                val gqlResponse = chipGetChatRatingListUseCase.getChatRatingList(chipGetChatRatingListUseCase.generateParam(input))
                val response = gqlResponse.getData<ChipGetChatRatingListResponse>(ChipGetChatRatingListResponse::class.java)

                onSuccessGetRatingList(response.chipGetChatRatingList)
            },
            onError = {
                onGetChatRatingListError(it)
                ChatbotNewRelicLogger.logNewRelic(
                    true,
                    messageId,
                    KEY_CHATBOT_GET_CHATLIST_RATING,
                    it
                )
            }
        )
    }

    private fun onChatRatingListSuccess(
        mappedPojo: ChatroomViewModel,
        onSuccessGetChat: (ChatroomViewModel, ChatReplies) -> Unit,
        chatReplies: ChatReplies,
        onGetChatRatingListMessageError: (String) -> Unit
    ): (ChipGetChatRatingListResponse.ChipGetChatRatingList?) -> Unit =
        { ratings ->
            updateMappedPojo(mappedPojo, ratings, onGetChatRatingListMessageError)
            onSuccessGetChat(mappedPojo, chatReplies)
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
                        msg is HelpFullQuestionsUiModel && rate.attachmentType == ChatbotGetExistingChatMapper.Companion.TYPE_OPTION_LIST.toIntOrZero()
                        -> (msg.helpfulQuestion?.caseChatId == rate.caseChatID)
                        msg is CsatOptionsUiModel && rate.attachmentType == ChatbotGetExistingChatMapper.Companion.TYPE_CSAT_OPTIONS.toIntOrZero()
                        -> (msg.csat?.caseChatId == rate.caseChatID)
                        else -> false
                    }
                }
                rateListMsgs.forEach {
                    if (it is HelpFullQuestionsUiModel) {
                        it.isSubmited = rate.isSubmitted ?: true
                    } else if (it is CsatOptionsUiModel) {
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
                    getChatRatingList(
                        inputList,
                        messageId,
                        onChatRatingListSuccess(mappedResponse, onSuccessGetChat, chatReplies, onGetChatRatingListMessageError)
                    )
                } else {
                    onSuccessGetChat(mappedResponse, chatReplies)
                }
            },
            onError = {
                onError.invoke(it)
                ChatbotNewRelicLogger.logNewRelicForGetChatReplies(
                    false,
                    messageId,
                    ChatbotConstant.NewRelic.KEY_CHATBOT_GET_EXISTING_CHAT_TOP,
                    it,
                    getExistingChatUseCase.minReplyTime,
                    getExistingChatUseCase.maxReplyTime
                )
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
                    getChatRatingList(
                        inputList,
                        messageId,
                        onChatRatingListSuccess(mappedResponse, onSuccessGetChat, chatReplies, onGetChatRatingListMessageError)
                    )
                } else {
                    onSuccessGetChat(mappedResponse, chatReplies)
                }
            },
            onError = {
                onError.invoke(it)
                ChatbotNewRelicLogger.logNewRelicForGetChatReplies(
                    false,
                    messageId,
                    ChatbotConstant.NewRelic.KEY_CHATBOT_GET_EXISTING_CHAT_BOTTOM,
                    it,
                    getExistingChatUseCase.minReplyTime,
                    getExistingChatUseCase.maxReplyTime
                )
            }
        )
    }
}
