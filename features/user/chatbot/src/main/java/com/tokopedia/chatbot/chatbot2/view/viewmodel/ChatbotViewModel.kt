package com.tokopedia.chatbot.chatbot2.view.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatReplies
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.CHAT_DIVIDER
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.OPEN_CSAT
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.SESSION_CHANGE
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_CSAT_OPTIONS
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_OPTION_LIST
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.UPDATE_TOOLBAR
import com.tokopedia.chatbot.ChatbotConstant.NewRelic.KEY_CHATBOT_SECURE_UPLOAD_AVAILABILITY
import com.tokopedia.chatbot.ChatbotConstant.ReplyBoxType.DYNAMIC_ATTACHMENT
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.chatbot2.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.chatbot2.data.livechatdivider.LiveChatDividerAttributes
import com.tokopedia.chatbot.chatbot2.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyPojo
import com.tokopedia.chatbot.chatbot2.data.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.chatbot2.data.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.chatbot.chatbot2.data.replybubble.ReplyBubbleAttributes
import com.tokopedia.chatbot.chatbot2.data.resolink.ResoLinkResponse
import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.chatbot2.data.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.chatbot.chatbot2.data.uploadEligibility.ChatbotUploadVideoEligibilityResponse
import com.tokopedia.chatbot.chatbot2.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.chatbot.chatbot2.data.uploadsecure.UploadSecureResponse
import com.tokopedia.chatbot.chatbot2.domain.mapper.ChatbotGetExistingChatMapper
import com.tokopedia.chatbot.chatbot2.domain.pojo.replyBox.BigReplyBoxAttribute
import com.tokopedia.chatbot.chatbot2.domain.pojo.replyBox.DynamicAttachment
import com.tokopedia.chatbot.chatbot2.domain.pojo.replyBox.ReplyBoxAttribute
import com.tokopedia.chatbot.chatbot2.domain.pojo.replyBox.SmallReplyBoxAttribute
import com.tokopedia.chatbot.chatbot2.domain.socket.ChatbotSendableWebSocketParam
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChatBotSecureImageUploadUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChatbotCheckUploadSecureUseCase
import com.tokopedia.chatbot.chatbot2.domain.usecase.ChatbotUploadVideoEligibilityUseCase
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
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotImageUploadFailureState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotOpenCsatState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSendChatRatingState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSocketErrorState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSocketReceiveEvent
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSubmitChatCsatState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotSubmitCsatRatingState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotUpdateToolbarState
import com.tokopedia.chatbot.chatbot2.view.viewmodel.state.ChatbotVideoUploadEligibilityState
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
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
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

typealias MediaUploadJobMap = Map<String, Job>
typealias MediaUploadResultMap = Map<String, ChatbotVideoUploadResult>
class ChatbotViewModel @Inject constructor(
    private val ticketListContactUsUseCase: TicketListContactUsUsecase,
    private val getTopBotNewSessionUseCase: GetTopBotNewSessionUseCase,
    private val checkUploadSecureUseCase: ChatbotCheckUploadSecureUseCase,
    private val chatbotVideoUploadVideoEligibilityUseCase: ChatbotUploadVideoEligibilityUseCase,
    private val getTickerDataUseCase: GetTickerDataUseCase,
    private val getResoLinkUseCase: GetResoLinkUseCase,
    private val submitCsatRatingUseCase: SubmitCsatRatingUseCase,
    private val chipSubmitHelpfulQuestionsUseCase: ChipSubmitHelpfulQuestionsUseCase,
    private val chipSubmitChatCsatUseCase: ChipSubmitChatCsatUseCase,
    private val sendChatRatingUseCase: SendChatRatingUseCase,
    var getExistingChatUseCase: GetExistingChatUseCase,
    var userSession: UserSessionInterface,
    private var chatBotWebSocketMessageMapper: com.tokopedia.chatbot.chatbot2.domain.mapper.ChatBotWebSocketMessageMapper,
    private val tkpdAuthInterceptor: TkpdAuthInterceptor,
    private val fingerprintInterceptor: FingerprintInterceptor,
    private val getExistingChatMapper: ChatbotGetExistingChatMapper,
    private val uploaderUseCase: UploaderUseCase,
    private val chipGetChatRatingListUseCase: ChipGetChatRatingListUseCase,
    private val chatbotWebSocket: ChatbotWebSocket,
    private val chatbotWebSocketStateHandler: ChatbotWebSocketStateHandler,
    private val chatBotSecureImageUploadUseCase: ChatBotSecureImageUploadUseCase,
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
    private val _videoUploadEligibility = MutableLiveData<ChatbotVideoUploadEligibilityState>()
    val videoUploadEligibility: LiveData<ChatbotVideoUploadEligibilityState>
        get() = _videoUploadEligibility
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

    init {
        observeMediaUrisForUpload()
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

    private fun onTicketListDataFail(throwable: Throwable) {
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
            throwable
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
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            KEY_CHATBOT_SECURE_UPLOAD_AVAILABILITY,
            throwable
        )
    }

    // Check for Video Upload Eligibility
    fun checkUploadVideoEligibility(msgId: String) {
        chatbotVideoUploadVideoEligibilityUseCase.cancelJobs()
        chatbotVideoUploadVideoEligibilityUseCase.getVideoUploadEligibility(
            ::onSuccessVideoUploadEligibility,
            ::onFailureVideoUploadEligibility,
            msgId
        )
    }

    private fun onSuccessVideoUploadEligibility(response: ChatbotUploadVideoEligibilityResponse) {
        _videoUploadEligibility.postValue(
            ChatbotVideoUploadEligibilityState.SuccessVideoUploadEligibility(
                response.topbotUploadVideoEligibility.dataVideoEligibility.isEligible
            )
        )
    }

    private fun onFailureVideoUploadEligibility(throwable: Throwable) {
        _videoUploadEligibility.postValue(
            ChatbotVideoUploadEligibilityState.FailureVideoUploadEligibility
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
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_TICKER,
            throwable
        )
    }

    private fun onSuccessGetTickerData(tickerData: com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse) {
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
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_CSAT_RATING,
            throwable
        )
    }

    // No need to do any actions on UI
    fun hitGqlforOptionList(messageId: String, selectedValue: Int, model: HelpFullQuestionsUiModel?) {
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
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_SUBMIT_HELPFULL_QUESTION,
            throwable
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
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_SUBMIT_CHAT_CSAT,
            throwable
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
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_GET_LINK_FOR_REDIRECTION,
            throwable
        )
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
        ChatbotNewRelicLogger.logNewRelic(
            false,
            messageId,
            ChatbotConstant.NewRelic.KEY_CHATBOT_SEND_RATING,
            throwable
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
            },
            onError = {
                _existingChatData.postValue(
                    ChatDataState.HandleFailureChatData(
                        it
                    )
                )

                ChatbotNewRelicLogger.logNewRelic(
                    false,
                    messageId,
                    ChatbotConstant.NewRelic.KEY_CHATBOT_GET_EXISTING_CHAT_FIRST_TIME,
                    it
                )
            }
        )
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

    fun getChatRatingData(mappedPojo: ChatroomViewModel): ChipGetChatRatingListInput {
        val input = ChipGetChatRatingListInput()
        for (message in mappedPojo.listChat) {
            if (message is HelpFullQuestionsUiModel) {
                input.list.add(
                    ChipGetChatRatingListInput.ChatRating(
                        TYPE_OPTION_LIST.toLongOrZero(),
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
                ChatbotNewRelicLogger.logNewRelic(
                    true,
                    messageId,
                    ChatbotConstant.NewRelic.KEY_CHATBOT_GET_CHATLIST_RATING,
                    it
                )
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
                            TYPE_OPTION_LIST.toLongOrZero()
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
                withTranscode = false
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
                Log.d("FATAL", "connectWebSocket: Exception occured here $it")
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
            WebsocketEvent.Event.EVENT_TOPCHAT_TYPING -> _socketReceiveMessageEvent.postValue(
                ChatbotSocketReceiveEvent.StartTypingEvent
            )
            WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING -> _socketReceiveMessageEvent.postValue(
                ChatbotSocketReceiveEvent.EndTypingEvent
            )
            WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE -> _socketReceiveMessageEvent.postValue(
                ChatbotSocketReceiveEvent.ReadEvent
            )
            WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE -> {
                val attachmentType = chatResponse?.attachment?.type
                if (attachmentType == SESSION_CHANGE ||
                    attachmentType == UPDATE_TOOLBAR ||
                    attachmentType == DYNAMIC_ATTACHMENT
                ) {
                    return
                }
                _socketReceiveMessageEvent.postValue(
                    ChatbotSocketReceiveEvent.ReplyMessageEvent(mapToVisitable(pojo))
                )

                if (attachmentType == "9") {
                    Log.d("FATAL", "mappingSocketEvent: ${mapToVisitable(pojo)}")
                }
                Log.d("FATAL", "mappingSocketEvent: value has been given here for $attachmentType")
                sendReadEventWebSocket(messageId)
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun handleAttachmentTypes(webSocketResponse: ChatWebSocketResponse, messageId: String) {
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
                else -> mapToVisitable(pojo)
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

        val replyBoxAttribute =
            dynamicAttachmentContents?.dynamicAttachmentAttribute?.replyBoxAttribute

        if (Attachment34RenderType.mapTypeToDeviceType(replyBoxAttribute?.renderTarget)
            == Attachment34RenderType.RenderAttachment34
        ) {
            when (replyBoxAttribute?.contentCode) {
                ChatbotConstant.ReplyBoxType.TYPE_BIG_REPLY_BOX -> {
                    convertToBigReplyBoxData(replyBoxAttribute.dynamicContent)
                }
                ChatbotConstant.ReplyBoxType.REPLY_BOX_TOGGLE_VALUE -> {
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

    fun validateHistoryForAttachment34(replyBoxAttribute: ReplyBoxAttribute?): Boolean {
        if (replyBoxAttribute == null) {
            return false
        }

        if (CheckDynamicAttachmentValidity.checkValidity(replyBoxAttribute.contentCode)) {
            when (replyBoxAttribute.contentCode) {
                ChatbotConstant.ReplyBoxType.TYPE_BIG_REPLY_BOX -> {
                    convertToBigReplyBoxData(replyBoxAttribute.dynamicContent)
                    return true
                }
                ChatbotConstant.ReplyBoxType.REPLY_BOX_TOGGLE_VALUE -> {
                    convertToSmallReplyBoxData(replyBoxAttribute.dynamicContent)
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

    private fun isValidReply(message: String) = message.isNotBlank()

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
                ChatbotNewRelicLogger.logNewRelic(
                    false,
                    messageId,
                    ChatbotConstant.NewRelic.KEY_SECURE_UPLOAD,
                    e
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

    fun cancelImageUpload() {
        chatBotSecureImageUploadUseCase.unsubscribe()
    }

    fun sendUploadedImageToWebsocket(json: JsonObject) {
        val interceptors = ArrayList<Interceptor>()
        interceptors.add(tkpdAuthInterceptor)
        interceptors.add(fingerprintInterceptor)
        chatbotWebSocket.send(json, interceptors)
    }

    override fun onCleared() {
        ticketListContactUsUseCase.cancelJobs()
        getTopBotNewSessionUseCase.cancelJobs()
        checkUploadSecureUseCase.cancelJobs()
        chatbotVideoUploadVideoEligibilityUseCase.cancelJobs()
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
