package com.tokopedia.chatbot.chatbot2.view.viewmodel

import android.text.TextUtils
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_BALLOON_ACTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY_SEND
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.SESSION_CHANGE
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_CHAT_SEPARATOR
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_CSAT_OPTIONS
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_CSAT_VIEW
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_HELPFULL_QUESTION
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_UPDATE_TOOLBAR
import com.tokopedia.chatbot.ChatbotConstant.DynamicAttachment.DYNAMIC_ATTACHMENT
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.chatbot2.data.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes
import com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.chatbot2.data.dynamicAttachment.BigReplyBoxAttribute
import com.tokopedia.chatbot.chatbot2.data.dynamicAttachment.DynamicAttachment
import com.tokopedia.chatbot.chatbot2.data.dynamicAttachment.DynamicAttachmentBodyAttributes
import com.tokopedia.chatbot.chatbot2.data.dynamicAttachment.SmallReplyBoxAttribute
import com.tokopedia.chatbot.chatbot2.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.chatbot2.data.livechatdivider.LiveChatDividerAttributes
import com.tokopedia.chatbot.chatbot2.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyPojo
import com.tokopedia.chatbot.chatbot2.data.quickreplysend.QuickReplySendAttachmentAttributes
import com.tokopedia.chatbot.chatbot2.data.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.chatbot2.data.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.data.replybubble.ReplyBubbleAttributes
import com.tokopedia.chatbot.chatbot2.data.resolink.ResoLinkResponse
import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.chatbot.chatbot2.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.chatbot.chatbot2.data.uploadsecure.UploadSecureResponse
import com.tokopedia.chatbot.chatbot2.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.chatbot2.domain.socket.ChatbotSendableWebSocketParam
import com.tokopedia.chatbot.chatbot2.domain.socket.ChatbotSendableWebSocketParam.generateParamDynamicAttachment108
import com.tokopedia.chatbot.chatbot2.domain.socket.ChatbotSendableWebSocketParam.generateParamDynamicAttachment108ForAcknowledgement
import com.tokopedia.chatbot.chatbot2.domain.socket.ChatbotSendableWebSocketParam.generateParamDynamicAttachmentText
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChatBotSecureImageUploadUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChatbotCheckUploadSecureUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChipGetChatRatingListUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChipSubmitChatCsatUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChipSubmitHelpfulQuestionsUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.GetExistingChatUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.GetResoLinkUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.GetTickerDataUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.GetTopBotNewSessionUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.SendChatRatingUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.SubmitCsatRatingUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.TicketListContactUsUsecase
import com.tokopedia.chatbot.chatbot2.domain.video.ChatbotVideoUploadResult
import com.tokopedia.chatbot.chatbot2.domain.video.VideoUploadData
import com.tokopedia.chatbot.chatbot2.util.ChatbotNewRelicLogger
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.chatbot2.view.util.Attachment34RenderType
import com.tokopedia.chatbot.chatbot2.view.util.CheckDynamicAttachmentValidity
import com.tokopedia.chatbot.chatbot2.view.util.helper.generateInput
import com.tokopedia.chatbot.chatbot2.view.util.helper.getEnvResoLink
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.BigReplyBoxState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatDataState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatRatingListState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotArticleEntryState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotChatSeparatorState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotDynamicAttachmentMediaButtonState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotImageUploadFailureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotOpenCsatState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotRejectReasonsState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSendChatRatingState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSocketErrorState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSocketReceiveEvent
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSubmitChatCsatState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSubmitCsatRatingState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotUpdateToolbarState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotVideoUploadFailureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.CheckUploadSecureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.GetTickerDataState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TicketListState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.TopBotNewSessionState
import com.tokopedia.chatbot.chatbot2.websocket.ChatWebSocketResponse
import com.tokopedia.chatbot.chatbot2.websocket.ChatbotWebSocket
import com.tokopedia.chatbot.chatbot2.websocket.ChatbotWebSocketAction
import com.tokopedia.chatbot.chatbot2.websocket.ChatbotWebSocketImpl
import com.tokopedia.chatbot.chatbot2.websocket.ChatbotWebSocketStateHandler
import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes
import com.tokopedia.chatbot.domain.pojo.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.domain.pojo.dynamicAttachment.MediaButtonAttribute
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor
import com.tokopedia.universal_sharing.usecase.ExtractBranchLinkUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
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

typealias MediaUploadJobMap = Map<String, Job>
typealias MediaUploadResultMap = Map<String, ChatbotVideoUploadResult>
class ChatbotViewModel @Inject constructor(
    private val ticketListContactUsUseCase: TicketListContactUsUsecase,
    private val getTopBotNewSessionUseCase: GetTopBotNewSessionUseCase,
    private val checkUploadSecureUseCase: ChatbotCheckUploadSecureUseCase,
    private val getTickerDataUseCase: GetTickerDataUseCase,
    private val getResoLinkUseCase: GetResoLinkUseCase,
    private val submitCsatRatingUseCase: SubmitCsatRatingUseCase,
    private val chipSubmitHelpfulQuestionsUseCase: ChipSubmitHelpfulQuestionsUseCase,
    private val chipSubmitChatCsatUseCase: ChipSubmitChatCsatUseCase,
    private val sendChatRatingUseCase: SendChatRatingUseCase,
    var getExistingChatUseCase: GetExistingChatUseCase,
    var userSession: UserSessionInterface,
    private var chatBotWebSocketMessageMapper: com.tokopedia.chatbot.chatbot2.domain.mapper.ChatBotWebSocketMessageMapper,
    private val tkpdAuthInterceptor: TkpdOldAuthInterceptor,
    private val fingerprintInterceptor: FingerprintInterceptor,
    private val getExistingChatMapper: ChatbotGetExistingChatMapper,
    private val uploaderUseCase: UploaderUseCase,
    private val chipGetChatRatingListUseCase: ChipGetChatRatingListUseCase,
    private val chatbotWebSocket: ChatbotWebSocket,
    private val chatbotWebSocketStateHandler: ChatbotWebSocketStateHandler,
    private val chatBotSecureImageUploadUseCase: ChatBotSecureImageUploadUseCase,
    private val extractBranchLinkUseCase: ExtractBranchLinkUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var listInterceptor: ArrayList<Interceptor> =
        arrayListOf(tkpdAuthInterceptor, fingerprintInterceptor)

    // GQL related
    private val _ticketList = MutableLiveData<TicketListState>()
    val ticketList: LiveData<TicketListState>
        get() = _ticketList
    private val _topBotNewSession = MutableLiveData<TopBotNewSessionState>()
    val topBotNewSession: LiveData<TopBotNewSessionState>
        get() = _topBotNewSession
    private val _checkUploadSecure = MutableLiveData<CheckUploadSecureState>()
    val checkUploadSecure: LiveData<CheckUploadSecureState>
        get() = _checkUploadSecure
    private val _tickerData = MutableLiveData<GetTickerDataState>()
    val tickerData: LiveData<GetTickerDataState>
        get() = _tickerData
    private val _submitCsatRating = MutableLiveData<ChatbotSubmitCsatRatingState>()
    val submitCsatRating: LiveData<ChatbotSubmitCsatRatingState>
        get() = _submitCsatRating
    private val _submitChatCsat = MutableLiveData<ChatbotSubmitChatCsatState>()
    val submitChatCsat: LiveData<ChatbotSubmitChatCsatState>
        get() = _submitChatCsat
    private val _stickyActionClick = MutableLiveData<Boolean>()
    val stickyActionClick: LiveData<Boolean>
        get() = _stickyActionClick
    private val _stickyActionGoToWebView = MutableLiveData<String>()
    val stickyActionGoToWebView: LiveData<String>
        get() = _stickyActionGoToWebView
    private val _toSendTextMessageForResoComponent = MutableLiveData<Boolean>()
    val toSendTextMessageForResoComponent: LiveData<Boolean>
        get() = _toSendTextMessageForResoComponent
    private val _sendChatRating = MutableLiveData<ChatbotSendChatRatingState>()
    val sendChatRating: LiveData<ChatbotSendChatRatingState>
        get() = _sendChatRating

    @VisibleForTesting
    val _existingChatData = MutableLiveData<ChatDataState>()
    val existingChatData: LiveData<ChatDataState>
        get() = _existingChatData
    private val _ratingListMessageError = MutableLiveData<ChatRatingListState>()
    val ratingListMessageError: LiveData<ChatRatingListState>
        get() = _ratingListMessageError
    private val _topChatData = MutableLiveData<ChatDataState>()
    val topChatData: LiveData<ChatDataState>
        get() = _topChatData
    private val _bottomChatData = MutableLiveData<ChatDataState>()
    val bottomChatData: LiveData<ChatDataState>
        get() = _bottomChatData
    private val _optionsListError = MutableLiveData<Boolean>()
    val optionsListError: LiveData<Boolean>
        get() = _optionsListError
    private val _resoLinkError = MutableLiveData<Boolean>()
    val resoLinkError: LiveData<Boolean>
        get() = _resoLinkError
    private val _imageUploadFailureData = MutableLiveData<ChatbotImageUploadFailureState.ImageUploadErrorBody>()
    val imageUploadFailureData: LiveData<ChatbotImageUploadFailureState.ImageUploadErrorBody>
        get() = _imageUploadFailureData
    private val _videoUploadFailure = MutableLiveData<ChatbotVideoUploadFailureState>()
    val videoUploadFailure: LiveData<ChatbotVideoUploadFailureState>
        get() = _videoUploadFailure
    private val _bigReplyBoxState = MutableLiveData<BigReplyBoxState>()
    val bigReplyBoxState: LiveData<BigReplyBoxState>
        get() = _bigReplyBoxState
    private val _smallReplyBoxDisabled = MutableLiveData<Boolean>()
    val smallReplyBoxDisabled: LiveData<Boolean>
        get() = _smallReplyBoxDisabled
    private val _dynamicAttachmentMediaUploadState = MutableLiveData<ChatbotDynamicAttachmentMediaButtonState>()
    val dynamicAttachmentMediaUploadState: LiveData<ChatbotDynamicAttachmentMediaButtonState>
        get() = _dynamicAttachmentMediaUploadState
    private val _dynamicAttachmentRejectReasonState = MutableLiveData<ChatbotRejectReasonsState>()
    val dynamicAttachmentRejectReasonState: LiveData<ChatbotRejectReasonsState>
        get() = _dynamicAttachmentRejectReasonState
    private val _typingBlockedState = MutableLiveData<Boolean>()
    val typingBlockedState: LiveData<Boolean>
        get() = _typingBlockedState

    // Video Upload Related
    @VisibleForTesting
    var mediaUploadJobs = MutableStateFlow<MediaUploadJobMap>(mapOf())
    var mediaUploadResults = MutableStateFlow<MediaUploadResultMap>(mapOf())

    @VisibleForTesting
    val shouldResetFailedUploadStatus = MutableStateFlow(false)
    val mediaUris = MutableStateFlow<List<VideoUploadData>>(emptyList())
    val throwable = Throwable()

    // Socket Related
    private var autoRetryJob: Job? = null
    var socketJob: Job? = null
    private var chatResponse: ChatSocketPojo? = null

    // Bot or Agent
    private val _receiverModeAgent = MutableLiveData<Boolean>()
    val receiverModeAgent: LiveData<Boolean>
        get() = _receiverModeAgent
    private val _updateToolbar = MutableLiveData<ChatbotUpdateToolbarState>()
    val updateToolbar: LiveData<ChatbotUpdateToolbarState>
        get() = _updateToolbar
    private val _openCsat = MutableLiveData<ChatbotOpenCsatState>()
    val openCsat: LiveData<ChatbotOpenCsatState>
        get() = _openCsat
    private val _chatSeparator = MutableLiveData<ChatbotChatSeparatorState>()
    val chatSeparator: LiveData<ChatbotChatSeparatorState>
        get() = _chatSeparator
    private val _isArticleEntryToSendData = MutableLiveData<ChatbotArticleEntryState>()
    val isArticleEntryToSendData: LiveData<ChatbotArticleEntryState>
        get() = _isArticleEntryToSendData
    private val _socketConnectionError = MutableLiveData<ChatbotSocketErrorState>()
    val socketConnectionError: LiveData<ChatbotSocketErrorState>
        get() = _socketConnectionError
    private val _socketReceiveMessageEvent = MutableLiveData<ChatbotSocketReceiveEvent>()
    val socketReceiveMessageEvent: LiveData<ChatbotSocketReceiveEvent>
        get() = _socketReceiveMessageEvent

    var pageSourceAccess = ""

    private val _applink = MutableLiveData<String>()
    val applink: LiveData<String>
        get() = _applink

    init {
        observeMediaUrisForUpload()
    }

    fun extractBranchLink(branchLink: String) {
        launchCatchError(
            block = {
                val deeplink = extractBranchLinkUseCase.invoke(branchLink).android_deeplink
                _applink.value = deeplink
            },
            onError = {
                _applink.value = ""
            }
        )
    }

    // Get Ticket List for Showing Contact Us Bottom Sheet
    fun getTicketList() {
        ticketListContactUsUseCase.cancelJobs()
        ticketListContactUsUseCase.getTicketList(
            ::onTicketListDataSuccess,
            ::onTicketListDataFail
        )
    }

    private fun onTicketListDataSuccess(inboxTicketListResponse: InboxTicketListResponse) {
        inboxTicketListResponse.ticket?.TicketData?.notice?.let {
            if (it.isActive) {
                _ticketList.postValue(TicketListState.BottomSheetData(it))
            } else {
                _ticketList.postValue(TicketListState.ShowContactUs)
            }
        } ?: kotlin.run {
            _ticketList.postValue(TicketListState.ShowContactUs)
        }
    }

    private fun onTicketListDataFail() {
        _ticketList.postValue(TicketListState.ShowContactUs)
    }

    // Check for New Session
    fun checkForSession(messageId: String) {
        getTopBotNewSessionUseCase.cancelJobs()
        getTopBotNewSessionUseCase.getTopBotUserSession(
            ::getTopBotNewSessionSuccess,
            ::getTopBotNewSessionFailure,
            messageId
        )
    }

    private fun getTopBotNewSessionSuccess(topBotNewSessionResponse: TopBotNewSessionResponse) {
        _topBotNewSession.postValue(
            TopBotNewSessionState.SuccessTopBotNewSession(
                topBotNewSessionResponse.topBotGetNewSession
            )
        )
    }

    private fun getTopBotNewSessionFailure(throwable: Throwable, messageId: String) {
        _topBotNewSession.postValue(
            TopBotNewSessionState.HandleFailureNewSession
        )
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_NEW_SESSION,
            throwable,
            pageSourceAccess
        )
    }

    // Check for upload secure , where do the value go
    fun checkUploadSecure(messageId: String) {
        checkUploadSecureUseCase.cancelJobs()
        checkUploadSecureUseCase.checkUploadSecure(
            ::checkUploadSecureSuccess,
            ::checkUploadSecureFailure,
            messageId
        )
    }

    private fun checkUploadSecureSuccess(checkUploadSecureResponse: CheckUploadSecureResponse) {
        _checkUploadSecure.postValue(
            CheckUploadSecureState.SuccessCheckUploadSecure(checkUploadSecureResponse)
        )
    }

    private fun checkUploadSecureFailure(throwable: Throwable, messageId: String) {
        _checkUploadSecure.postValue(
            CheckUploadSecureState.HandleFailureCheckUploadSecure
        )
    }

    // Get Ticker Data
    fun showTickerData(messageId: String) {
        getTickerDataUseCase.cancelJobs()
        getTickerDataUseCase.getTickerData(
            ::onSuccessGetTickerData,
            ::onErrorGetTickerData,
            messageId
        )
    }

    private fun onErrorGetTickerData(throwable: Throwable, messageId: String) {
        _tickerData.postValue(
            GetTickerDataState.HandleTickerDataFailure(throwable)
        )
    }

    private fun onSuccessGetTickerData(tickerData: com.tokopedia.chatbot.chatbot2.data.tickerData.TickerDataResponse) {
        tickerData.chipGetActiveTickerV4?.data?.let {
            _tickerData.postValue(
                GetTickerDataState.SuccessTickerData(it)
            )
        } ?: kotlin.run {
            _tickerData.postValue(
                GetTickerDataState.HandleTickerDataFailure(throwable)
            )
        }
    }

    fun submitCsatRating(messageId: String, inputItem: com.tokopedia.chatbot.chatbot2.data.csatRating.csatInput.InputItem) {
        submitCsatRatingUseCase.cancelJobs()

        submitCsatRatingUseCase.submitCsatRating(
            ::onSuccessSubmitCsatRating,
            ::onErrorSubmitCsatRating,
            inputItem,
            messageId
        )
    }

    private fun onSuccessSubmitCsatRating(submitCsatGqlResponse: com.tokopedia.chatbot.chatbot2.data.csatRating.csatResponse.SubmitCsatGqlResponse) {
        _submitCsatRating.postValue(
            ChatbotSubmitCsatRatingState.SuccessChatbotSubmtiCsatRating(
                submitCsatGqlResponse.submitRatingCSAT?.data?.message.toString()
            )
        )
    }

    private fun onErrorSubmitCsatRating(throwable: Throwable, messageId: String) {
        _submitCsatRating.postValue(
            ChatbotSubmitCsatRatingState.HandleFailureChatbotSubmitCsatRating(throwable)
        )
    }

    // No need to do any actions on UI
    fun hitGqlforOptionList(messageId: String, selectedValue: Long, model: HelpFullQuestionsUiModel?) {
        val input = generateInput(selectedValue, model)
        chipSubmitHelpfulQuestionsUseCase.cancelJobs()
        chipSubmitHelpfulQuestionsUseCase.chipSubmitHelpfulQuestions(
            ::onErrorOptionList,
            input,
            messageId
        )
    }

    private fun onErrorOptionList(throwable: Throwable, messageId: String) {
        _optionsListError.postValue(
            true
        )
    }

    fun submitChatCsat(messageId: String, input: ChipSubmitChatCsatInput) {
        chipSubmitChatCsatUseCase.cancelJobs()
        chipSubmitChatCsatUseCase.chipSubmitChatCsat(
            ::onSuccessSubmitChatCsat,
            ::onFailureSubmitChatCsat,
            input,
            messageId
        )
    }

    private fun onSuccessSubmitChatCsat(chipSubmitChatCsatResponse: ChipSubmitChatCsatResponse) {
        _submitChatCsat.postValue(
            ChatbotSubmitChatCsatState.HandleChatbotSubmitChatCsatSuccess(
                chipSubmitChatCsatResponse.chipSubmitChatCSAT?.csatSubmitData?.toasterMessage ?: ""
            )
        )
    }

    private fun onFailureSubmitChatCsat(throwable: Throwable, messageId: String) {
        _submitChatCsat.postValue(
            ChatbotSubmitChatCsatState.FailureChatbotSubmitChatCsat(throwable)
        )
    }

    fun checkLinkForRedirectionForStickyActionClick(
        messageId: String,
        invoiceRefNum: String
    ) {
        getResoLinkUseCase.cancelJobs()
        getResoLinkUseCase.getResolutionLink(
            ::onSuccessRedirectionForStickyActionClick,
            ::onFailureForResoLink,
            invoiceRefNum,
            messageId
        )
    }

    private fun onSuccessRedirectionForStickyActionClick(resoLinkResponse: ResoLinkResponse) {
        val orderList =
            resoLinkResponse.getResolutionLink?.resolutionLinkData?.orderList?.firstOrNull()
        if (orderList?.resoList?.isNotEmpty() == true) {
            _stickyActionClick.postValue(true)
        } else {
            _stickyActionClick.postValue(false)
        }
        val link = orderList?.dynamicLink ?: ""
        val resoLink = getEnvResoLink(link)
        if (resoLink.isNotEmpty()) {
            _stickyActionGoToWebView.postValue(resoLink)
        }
    }

    private fun onFailureForResoLink(throwable: Throwable, messageId: String) {
        _resoLinkError.postValue(true)
    }

    fun checkToSendMessageForResoLink(
        messageId: String,
        invoiceRefNum: String
    ) {
        getResoLinkUseCase.cancelJobs()
        getResoLinkUseCase.getResolutionLink(
            ::onSuccessCheckToSendMessageForResoLink,
            ::onFailureForResoLink,
            invoiceRefNum,
            messageId
        )
    }

    private fun onSuccessCheckToSendMessageForResoLink(resoLinkResponse: ResoLinkResponse) {
        val orderList =
            resoLinkResponse.getResolutionLink?.resolutionLinkData?.orderList?.firstOrNull()
        if (orderList?.resoList?.isNotEmpty() == true) {
            _toSendTextMessageForResoComponent.postValue(true)
        } else {
            _toSendTextMessageForResoComponent.postValue(false)
        }
    }

    fun sendRating(messageId: String, rating: Int, element: ChatRatingUiModel) {
        sendChatRatingUseCase.sendChatRating(
            ::onSuccessSendRating,
            ::onFailureSendRating,
            messageId,
            rating,
            element
        )
    }

    private fun onFailureSendRating(throwable: Throwable, messageId: String) {
        _sendChatRating.postValue(
            ChatbotSendChatRatingState.HandleErrorSendChatRating(throwable)
        )
    }

    private fun onSuccessSendRating(pojo: com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo) {
        _sendChatRating.postValue(
            ChatbotSendChatRatingState.HandleSuccessChatbotSendChatRating(pojo)
        )
    }

    fun getExistingChat(messageId: String) {
        if (messageId.isEmpty()) {
            return
        }

        launchCatchError(
            block = {
                val response = getExistingChatUseCase.getFirstPageChat(messageId)
                val mappedResponse = getExistingChatMapper.map(response)
                val chatReplies = response.chatReplies
                val inputList = getChatRatingData(mappedResponse)
                if (inputList.list.isNotEmpty()) {
                    getChatRatingList(
                        inputList,
                        messageId,
                        chatReplies,
                        mappedResponse,
                        _existingChatData
                    )
                } else {
                    _existingChatData.postValue(
                        ChatDataState.SuccessChatDataState(mappedResponse, chatReplies)
                    )
                }
                checkIsTypingBlockedDataFromExistingChat(response)
            },
            onError = {
                _existingChatData.postValue(
                    ChatDataState.HandleFailureChatData(
                        it
                    )
                )
            }
        )
    }

    private fun checkIsTypingBlockedDataFromExistingChat(data: GetExistingChatPojo) {
        data.chatReplies.list.forEach { chatRepliesItem ->
            chatRepliesItem.chats.forEach { chat ->
                chat.replies.forEach { reply ->
                    when (reply.attachment.type.toString()) {
                        TYPE_CHAT_BALLOON_ACTION -> {
                            val pojoAttribute = GsonBuilder().create().fromJson(
                                reply.attachment.attributes,
                                ChatActionBalloonSelectionAttachmentAttributes::class.java
                            )
                            handleTypingBlockState(pojoAttribute.isTypingBlockedOnButtonSelect)
                        }
                        TYPE_QUICK_REPLY_SEND -> {
                            val pojoAttribute = GsonBuilder().create().fromJson(
                                reply.attachment.attributes,
                                QuickReplySendAttachmentAttributes::class.java
                            )
                            handleTypingBlockState(pojoAttribute.isTypingBlocked)
                        }
                    }
                }
            }
        }
    }

    fun getBottomChat(
        messageId: String
    ) {
        if (messageId.isEmpty()) {
            return
        }

        launchCatchError(
            block = {
                val gqlResponse = getExistingChatUseCase.getBottomChat(messageId)
                val chatReplies = gqlResponse.chatReplies
                val mappedResponse = getExistingChatMapper.map(gqlResponse)
                val inputList = getChatRatingData(mappedResponse)
                if (inputList.list.isNotEmpty()) {
                    getChatRatingList(
                        inputList,
                        messageId,
                        chatReplies,
                        mappedResponse,
                        _bottomChatData
                    )
                } else {
                    _bottomChatData.postValue(
                        ChatDataState.SuccessChatDataState(mappedResponse, chatReplies)
                    )
                }
            },
            onError = {
                _bottomChatData.postValue(
                    ChatDataState.HandleFailureChatData(
                        it
                    )
                )
            }
        )
    }

    fun getTopChat(
        messageId: String
    ) {
        if (messageId.isEmpty()) {
            return
        }
        launchCatchError(
            block = {
                val gqlResponse = getExistingChatUseCase.getTopChat(messageId)
                val chatReplies = gqlResponse.chatReplies
                val mappedResponse = getExistingChatMapper.map(gqlResponse)
                val inputList = getChatRatingData(mappedResponse)
                if (inputList.list.isNotEmpty()) {
                    getChatRatingList(
                        inputList,
                        messageId,
                        chatReplies,
                        mappedResponse,
                        _topChatData
                    )
                } else {
                    _topChatData.postValue(
                        ChatDataState.SuccessChatDataState(mappedResponse, chatReplies)
                    )
                }
            },
            onError = {
                _topChatData.postValue(
                    ChatDataState.HandleFailureChatData(
                        it
                    )
                )
            }
        )
    }

    fun getChatRatingData(mappedPojo: ChatroomViewModel): ChipGetChatRatingListInput {
        val input = ChipGetChatRatingListInput()
        for (message in mappedPojo.listChat) {
            if (message is HelpFullQuestionsUiModel) {
                input.list.add(
                    ChipGetChatRatingListInput.ChatRating(
                        TYPE_HELPFULL_QUESTION.toLongOrZero(),
                        message.helpfulQuestion?.caseChatId ?: ""
                    )
                )
            } else if (message is CsatOptionsUiModel) {
                input.list.add(
                    ChipGetChatRatingListInput.ChatRating(
                        TYPE_CSAT_OPTIONS.toLongOrZero(),
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
        chatReplies: ChatReplies,
        mappedResponse: ChatroomViewModel,
        chatLiveData: MutableLiveData<ChatDataState>
    ) {
        launchCatchError(
            block = {
                val gqlResponse = chipGetChatRatingListUseCase.getChatRatingList(
                    chipGetChatRatingListUseCase.generateParam(inputList)
                )
                val response = gqlResponse.getData<ChipGetChatRatingListResponse>(
                    ChipGetChatRatingListResponse::class.java
                )

                updateMappedPojo(mappedResponse, response.chipGetChatRatingList)
                chatLiveData.postValue(
                    ChatDataState.SuccessChatDataState(
                        mappedResponse,
                        chatReplies
                    )
                )
            },
            onError = {
            }
        )
    }

    @VisibleForTesting
    fun updateMappedPojo(
        mappedPojo: ChatroomViewModel,
        ratings: ChipGetChatRatingListResponse.ChipGetChatRatingList?
    ) {
        if (ratings?.ratingListData?.isSuccess == 1L) {
            for (rate in ratings.ratingListData.list ?: listOf()) {
                val rateListMsgs = mappedPojo.listChat.filter { msg ->
                    when {
                        msg is HelpFullQuestionsUiModel &&
                            rate.attachmentType ==
                            TYPE_HELPFULL_QUESTION.toLongOrZero()
                        -> (msg.helpfulQuestion?.caseChatId == rate.caseChatID)
                        msg is CsatOptionsUiModel &&
                            rate.attachmentType ==
                            TYPE_CSAT_OPTIONS.toLongOrZero()
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
            _ratingListMessageError.postValue(
                ChatRatingListState.HandleFailureChatRatingList(
                    ratings?.messageError?.get(0) ?: ""
                )
            )
        }
    }

    fun clearGetChatUseCase() {
        getExistingChatUseCase.reset()
    }

    fun setBeforeReplyTime(createTime: String) {
        getExistingChatUseCase.updateMinReplyTime(createTime)
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

    @VisibleForTesting
    suspend fun tryUploadMedia(data: Pair<Boolean, List<VideoUploadData>>) {
        val (shouldResetFailedUploadStatus, videoData) = data
        if (shouldResetFailedUploadStatus) {
            mediaUploadResults.value = mediaUploadResults.value.filterValues { uploadResult ->
                uploadResult is ChatbotVideoUploadResult.Success
            }
            this@ChatbotViewModel.shouldResetFailedUploadStatus.value = false
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
                            it.startTime,
                            it.videoUploadUiModel
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
        startTime: String,
        videoUploadUiModel: VideoUploadUiModel
    ): Job {
        return launchCatchError(block = {
            val filePath = File(uri)
            val params = uploaderUseCase.createParams(
                sourceId = ChatbotConstant.VideoUpload.SOURCE_ID_FOR_VIDEO_UPLOAD,
                filePath = filePath,
                withTranscode = true
            )
            val uploadMediaResult = uploaderUseCase(params).let {
                when (it) {
                    is UploadResult.Success -> {
                        sendVideoAttachment(it.videoUrl, startTime, messageId)
                        ChatbotVideoUploadResult.Success(it.uploadId, it.videoUrl)
                    }
                    is UploadResult.Error -> {
                        _videoUploadFailure.postValue(
                            ChatbotVideoUploadFailureState.ChatbotVideoUploadFailure(
                                videoUploadUiModel,
                                it.message
                            )
                        )
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

    fun sendVideoAttachment(filePath: String, startTime: String, messageId: String) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.generateParamSendVideoAttachment(
                filePath,
                startTime,
                messageId
            ),
            listInterceptor
        )
    }

    fun cancelVideoUpload(file: String, sourceId: String) {
        launchCatchError(
            block = {
                uploaderUseCase.abortUpload(
                    filePath = file,
                    sourceId = sourceId
                )
                mediaUploadJobs.value.get(file)?.cancel()
            },
            onError = {
            }
        )
    }

    fun connectWebSocket(messageId: String) {
        socketJob?.cancel()

        val webSocketUrl = ChatbotConstant.ChatbotUrl.getPathWebsocket(userSession.deviceId, userSession.userId)

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
                FirebaseCrashlytics.getInstance().recordException(it)
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
        sendReadEventWebSocket(messageId)
        _socketConnectionError.postValue(
            ChatbotSocketErrorState.SocketConnectionSuccessful
        )
        _isArticleEntryToSendData.postValue(
            ChatbotArticleEntryState.ToSendArticleUponEntry
        )
        chatbotWebSocketStateHandler.retrySucceed()
    }

    private fun handleNewMessage(message: ChatWebSocketResponse, messageId: String) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            Timber.d("Chatbot Socket Message Received: $message")
        }
        handleAttachmentTypes(message, messageId)
    }

    private fun handleSocketFailure(messageId: String) {
        _socketConnectionError.postValue(
            ChatbotSocketErrorState.SocketConnectionError
        )
        retryConnectToWebSocket(messageId)
    }

    private fun handleSocketClosed(code: Int, messageId: String) {
        if (code != ChatbotWebSocketImpl.CODE_NORMAL_CLOSURE) {
            retryConnectToWebSocket(messageId)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun sendReadEventWebSocket(messageId: String) {
        chatbotWebSocket.send(
            ChatbotSendableWebSocketParam.getReadMessageWebSocket(messageId),
            listInterceptor
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun retryConnectToWebSocket(messageId: String) {
        chatbotWebSocket.close()
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
                FirebaseCrashlytics.getInstance().recordException(it)
            }
        )
    }

    private fun mappingSocketEvent(webSocketResponse: ChatWebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo =
            Gson().fromJson(webSocketResponse.jsonObject, ChatSocketPojo::class.java)

        when (webSocketResponse.code) {
            WebsocketEvent.Event.EVENT_TOPCHAT_TYPING -> updateLiveDataOnMainThread(
                ChatbotSocketReceiveEvent.StartTypingEvent
            )
            WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING -> updateLiveDataOnMainThread(
                ChatbotSocketReceiveEvent.EndTypingEvent
            )
            WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE -> updateLiveDataOnMainThread(
                ChatbotSocketReceiveEvent.ReadEvent
            )
            WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE -> {
                val attachmentType = chatResponse?.attachment?.type
                if (attachmentType == SESSION_CHANGE ||
                    attachmentType == TYPE_UPDATE_TOOLBAR ||
                    checkForDynamicAttachment(pojo, attachmentType)
                ) {
                    return
                }

                if (attachmentType == TYPE_HELPFULL_QUESTION || attachmentType == TYPE_CSAT_OPTIONS) {
                    sendNewRelicLogRelatedToCsat(pojo, messageId)
                }

                updateLiveDataOnMainThread(ChatbotSocketReceiveEvent.ReplyMessageEvent(mapToVisitable(pojo)))
                sendReadEventWebSocket(messageId)
            }
        }
    }

    private fun checkForDynamicAttachment(pojo: ChatSocketPojo, attachmentType: String?): Boolean {
        if (attachmentType != DYNAMIC_ATTACHMENT) {
            return false
        }
        val dynamicAttachment = GsonBuilder().create().fromJson(
            pojo.attachment?.attributes,
            DynamicAttachment::class.java
        )
        val contentCode = dynamicAttachment?.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes?.contentCode
        return !ChatbotConstant.DynamicAttachment.PROCESS_TO_VISITABLE_DYNAMIC_ATTACHMENT.contains(contentCode)
    }

    private fun updateLiveDataOnMainThread(value: ChatbotSocketReceiveEvent) {
        viewModelScope.launch(dispatcher.main) {
            _socketReceiveMessageEvent.value = value
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun handleAttachmentTypes(webSocketResponse: ChatWebSocketResponse, messageId: String) {
        val pojo: ChatSocketPojo =
            Gson().fromJson(webSocketResponse.jsonObject, ChatSocketPojo::class.java)
        if (pojo.msgId != messageId) return
        chatResponse = pojo

        mappingSocketEvent(webSocketResponse, messageId)
        checkIsTypingBlockedDataFromWebSocket(pojo)

        val attachmentType = chatResponse?.attachment?.type
        if (attachmentType != null) {
            when (attachmentType) {
                TYPE_CSAT_VIEW -> handleOpenCsatAttachment(webSocketResponse)
                TYPE_UPDATE_TOOLBAR -> handleUpdateToolbarAttachment()
                TYPE_CHAT_SEPARATOR -> handleChatDividerAttachment()
                SESSION_CHANGE -> handleSessionChangeAttachment()
                DYNAMIC_ATTACHMENT -> handleDynamicAttachment34(pojo)
                else -> mapToVisitable(pojo)
            }
        }
    }

    private fun checkIsTypingBlockedDataFromWebSocket(chatResponse: ChatSocketPojo) {
        val attachmentType = chatResponse.attachment?.type
        if (attachmentType != null) {
            when (attachmentType) {
                TYPE_CHAT_BALLOON_ACTION -> {
                    val pojoAttribute = GsonBuilder().create().fromJson(
                        chatResponse.attachment?.attributes,
                        ChatActionBalloonSelectionAttachmentAttributes::class.java
                    )
                    handleTypingBlockState(pojoAttribute.isTypingBlockedOnButtonSelect)
                }
                TYPE_QUICK_REPLY_SEND -> {
                    val pojoAttribute = GsonBuilder().create().fromJson(
                        chatResponse.attachment?.attributes,
                        QuickReplySendAttachmentAttributes::class.java
                    )
                    handleTypingBlockState(pojoAttribute.isTypingBlocked)
                }
            }
        }
    }

    private fun handleOpenCsatAttachment(webSocketResponse: ChatWebSocketResponse) {
        val csatResponse: WebSocketCsatResponse = Gson().fromJson(
            webSocketResponse.jsonObject,
            WebSocketCsatResponse::class.java
        )
        _openCsat.postValue(
            ChatbotOpenCsatState.ShowCsat(
                csatResponse
            )
        )
    }

    private fun handleUpdateToolbarAttachment() {
        val tool =
            Gson().fromJson(chatResponse?.attachment?.attributes, ToolbarAttributes::class.java)
        _updateToolbar.postValue(
            ChatbotUpdateToolbarState.UpdateToolbar(
                tool.profileName,
                tool.profileImage,
                tool.badgeImage
            )
        )
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
        _chatSeparator.postValue(
            ChatbotChatSeparatorState.ChatSeparator(
                model,
                getLiveChatQuickReply()
            )
        )
    }

    private fun handleSessionChangeAttachment() {
        val agentMode: ReplyBubbleAttributes = Gson().fromJson(
            chatResponse?.attachment?.attributes,
            ReplyBubbleAttributes::class.java
        )
        handleSessionChange(agentMode)
    }

    private fun handleSessionChange(agentMode: ReplyBubbleAttributes) {
        if (agentMode.sessionChange.mode == ChatbotConstant.MODE_AGENT) {
            _receiverModeAgent.postValue(
                true
            )
        } else if (agentMode.sessionChange.mode == ChatbotConstant.MODE_BOT) {
            _receiverModeAgent.postValue(
                false
            )
        }
    }

    @VisibleForTesting
    fun handleDynamicAttachment34(pojo: ChatSocketPojo) {
        val dynamicAttachmentContents =
            Gson().fromJson(pojo.attachment?.attributes, DynamicAttachment::class.java)

        val dynamicAttachmentAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes

        if (Attachment34RenderType.mapTypeToDeviceType(dynamicAttachmentAttribute?.renderTarget)
            == Attachment34RenderType.RenderAttachment34
        ) {
            when (dynamicAttachmentAttribute?.contentCode) {
                ChatbotConstant.DynamicAttachment.TYPE_BIG_REPLY_BOX -> {
                    convertToBigReplyBoxData(dynamicAttachmentAttribute.dynamicContent)
                }
                ChatbotConstant.DynamicAttachment.REPLY_BOX_TOGGLE_VALUE -> {
                    convertToSmallReplyBoxData(dynamicAttachmentAttribute.dynamicContent)
                }
                ChatbotConstant.DynamicAttachment.MEDIA_BUTTON_TOGGLE -> {
                    convertToMediaButtonToggleData(dynamicAttachmentAttribute.dynamicContent)
                }
                ChatbotConstant.DynamicAttachment.DYNAMIC_REJECT_REASON -> {
                    convertToRejectReasonsData(dynamicAttachmentAttribute.dynamicContent)
                }
                else -> {
                    // need to show fallback message
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

    private fun convertToMediaButtonToggleData(dynamicContent: String?) {
        if (dynamicContent == null) {
            return
        }

        val mediaButtonToggleContent = Gson().fromJson(
            dynamicContent,
            MediaButtonAttribute::class.java
        )

        handleMediaButtonWS(mediaButtonToggleContent)
    }

    private fun convertToRejectReasonsData(dynamicContent: String?) {
        if (dynamicContent == null) {
            return
        }
        val rejectReasonData = Gson().fromJson(
            dynamicContent,
            DynamicAttachmentRejectReasons::class.java
        )

        handleDynamicAttachmentRejectReasons(rejectReasonData)
    }

    private fun handleMediaButtonWS(mediaButtonToggleContent: MediaButtonAttribute) {
        if (mediaButtonToggleContent.isMediaButtonEnabled) {
            _dynamicAttachmentMediaUploadState.postValue(
                ChatbotDynamicAttachmentMediaButtonState.OnReceiveMediaButtonAttachment(
                    toShowAddAttachmentButton = true,
                    toShowImageUploadButton = mediaButtonToggleContent.buttons?.isUploadImageEnabled ?: false,
                    toShowVideoUploadButton = mediaButtonToggleContent.buttons?.isUploadVideoEnabled ?: false
                )
            )
        } else {
            _dynamicAttachmentMediaUploadState.postValue(
                ChatbotDynamicAttachmentMediaButtonState.OnReceiveMediaButtonAttachment(
                    toShowAddAttachmentButton = false,
                    toShowImageUploadButton = false,
                    toShowVideoUploadButton = false
                )
            )
        }
    }

    fun handleDynamicAttachmentRejectReasons(rejectReasonData: DynamicAttachmentRejectReasons) {
        _dynamicAttachmentRejectReasonState.postValue(
            ChatbotRejectReasonsState.ChatbotRejectReasonData(
                rejectReasonData
            )
        )
    }

    private fun handleBigReplyBoxWS(bigReplyBoxContent: BigReplyBoxAttribute) {
        if (bigReplyBoxContent.isActive) {
            _bigReplyBoxState.postValue(
                BigReplyBoxState.BigReplyBoxContents(
                    bigReplyBoxContent.title,
                    bigReplyBoxContent.placeholder
                )
            )
        }
    }

    private fun handleSmallReplyBoxWS(smallReplyBoxContent: SmallReplyBoxAttribute) {
        if (smallReplyBoxContent.isHidden) {
            _smallReplyBoxDisabled.postValue(true)
        } else {
            _smallReplyBoxDisabled.postValue(false)
        }
    }

    private fun handleTypingBlockState(isTypingBlocked: Boolean) {
        _typingBlockedState.postValue(isTypingBlocked)
    }

    fun validateHistoryForAttachment34(dynamicAttachmentBodyAttributes: DynamicAttachmentBodyAttributes?): Boolean {
        if (dynamicAttachmentBodyAttributes == null) {
            return false
        }

        if (CheckDynamicAttachmentValidity.checkValidity(dynamicAttachmentBodyAttributes.contentCode)) {
            when (dynamicAttachmentBodyAttributes.contentCode) {
                ChatbotConstant.DynamicAttachment.TYPE_BIG_REPLY_BOX -> {
                    convertToBigReplyBoxData(dynamicAttachmentBodyAttributes.dynamicContent)
                    return true
                }
                ChatbotConstant.DynamicAttachment.REPLY_BOX_TOGGLE_VALUE -> {
                    convertToSmallReplyBoxData(dynamicAttachmentBodyAttributes.dynamicContent)
                    return true
                }
            }
        }
        return false
    }

    @VisibleForTesting
    fun getLiveChatQuickReply(): List<QuickReplyUiModel> {
        val quickReplyListPojo = GsonBuilder().create()
            .fromJson(
                chatResponse?.attachment?.attributes,
                QuickReplyAttachmentAttributes::class.java
            )
        return if (quickReplyListPojo == null) {
            emptyList()
        } else if (quickReplyListPojo.quickReplies.isEmpty()) {
            emptyList()
        } else {
            addItemsToQuickReplyList(quickReplyListPojo.quickReplies)
        }
    }

    private fun addItemsToQuickReplyList(quickReplies: List<QuickReplyPojo>): ArrayList<QuickReplyUiModel> {
        val list = ArrayList<QuickReplyUiModel>()
        for (pojo in quickReplies) {
            if (!TextUtils.isEmpty(pojo.text)) {
                list.add(QuickReplyUiModel(pojo.text, pojo.value, pojo.action))
            }
        }
        return list
    }

    fun sendActionBubble(
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

    fun destroyWebSocket() {
        socketJob?.cancel()
        autoRetryJob?.cancel()
        chatbotWebSocket.close()
    }

    fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return chatBotWebSocketMessageMapper.map(pojo)
    }

    fun sendInvoiceAttachment(
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

    fun sendQuickReply(
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

    fun sendQuickReplyInvoice(
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

    fun sendMessageWithWebsocket(
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

    fun sendDynamicAttachmentText(
        messageId: String,
        bubbleUiModel: ChatActionBubbleUiModel,
        startTime: String,
        opponentId: String
    ) {
        chatbotWebSocket.send(
            generateParamDynamicAttachmentText(
                messageId,
                bubbleUiModel,
                startTime,
                opponentId
            ),
            listInterceptor
        )
    }

    private fun isValidReply(message: String) = message.isNotBlank()

    fun sendDynamicAttachment108(
        reasonCodeList: List<Long>,
        reason: String,
        messageId: String,
        toUid: String,
        startTime: String,
        helpfulQuestion: DynamicAttachmentRejectReasons.RejectReasonHelpfulQuestion?,
        index: Int,
        isSubmitAfterOpenForm: Boolean
    ) {
        chatbotWebSocket.send(
            generateParamDynamicAttachment108(
                reasonCodeList,
                reason,
                messageId,
                toUid,
                startTime,
                helpfulQuestion,
                index,
                isSubmitAfterOpenForm
            ),
            listInterceptor
        )
    }

    fun sendDynamicAttachment108ForAcknowledgement(
        messageId: String,
        toUid: String,
        model: QuickReplyUiModel,
        startTime: String,
        index: Int,
        isSubmitAfterOpenForm: Boolean
    ) {
        chatbotWebSocket.send(
            generateParamDynamicAttachment108ForAcknowledgement(
                messageId,
                toUid,
                model,
                startTime,
                index,
                isSubmitAfterOpenForm
            ),
            listInterceptor
        )
    }

    fun sendMessage(
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

    fun uploadImageSecureUpload(
        imageUploadUiModel: ImageUploadUiModel,
        messageId: String,
        path: String?
    ) {
        chatBotSecureImageUploadUseCase.setRequestParams(messageId, path ?: "")
        chatBotSecureImageUploadUseCase.execute(object : Subscriber<Map<Type?, RestResponse?>?>() {
            override fun onCompleted() {
                Timber.d("Enter onCompleted")
            }

            override fun onError(e: Throwable) {
                _imageUploadFailureData.postValue(
                    ChatbotImageUploadFailureState.ImageUploadErrorBody(
                        throwable,
                        imageUploadUiModel
                    )
                )
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
                            imageUploadUiModel.startTime,
                            userSession.name
                        )
                )
            }
        })
    }

    fun setPageSourceValue(pageSource: String) {
        pageSourceAccess = pageSource
    }

    fun cancelImageUpload() {
        chatBotSecureImageUploadUseCase.unsubscribe()
    }

    fun sendUploadedImageToWebsocket(json: JsonObject) {
        val interceptors = ArrayList<Interceptor>()
        interceptors.add(tkpdAuthInterceptor)
        interceptors.add(fingerprintInterceptor)
        chatbotWebSocket.send(json, interceptors)
    }

    private fun sendNewRelicLogRelatedToCsat(pojo: ChatSocketPojo, messageId: String) {
        val attachmentType = chatResponse?.attachment?.type
        if (attachmentType == TYPE_HELPFULL_QUESTION) {
            val helpFullQuestionPojo = GsonBuilder().create()
                .fromJson<HelpFullQuestionPojo>(
                    pojo.attachment?.attributes,
                    HelpFullQuestionPojo::class.java
                )
            com.tokopedia.chatbot.util.ChatbotNewRelicLogger.logNewRelicForCSAT(
                messageId,
                TYPE_HELPFULL_QUESTION,
                helpFullQuestionPojo.helpfulQuestion?.caseId.toBlankOrString(),
                helpFullQuestionPojo.helpfulQuestion?.caseChatId.toBlankOrString()
            )
        } else if (attachmentType == TYPE_CSAT_OPTIONS) {
            val csatAttributesPojo = GsonBuilder().create()
                .fromJson<CsatAttributesPojo>(
                    pojo.attachment?.attributes,
                    CsatAttributesPojo::class.java
                )
            com.tokopedia.chatbot.util.ChatbotNewRelicLogger.logNewRelicForCSAT(
                messageId,
                TYPE_CSAT_OPTIONS,
                csatAttributesPojo.csat?.caseId.toBlankOrString(),
                csatAttributesPojo.csat?.caseChatId.toBlankOrString()
            )
        }
    }

    override fun onCleared() {
        ticketListContactUsUseCase.cancelJobs()
        getTopBotNewSessionUseCase.cancelJobs()
        checkUploadSecureUseCase.cancelJobs()
        getTickerDataUseCase.cancelJobs()
        getResoLinkUseCase.cancelJobs()
        submitCsatRatingUseCase.cancelJobs()
        chipSubmitHelpfulQuestionsUseCase.cancelJobs()
        chipSubmitChatCsatUseCase.cancelJobs()
        sendChatRatingUseCase.cancelJobs()
        chatBotSecureImageUploadUseCase.unsubscribe()
        destroyWebSocket()
        super.onCleared()
    }
}
