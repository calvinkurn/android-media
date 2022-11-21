package com.tokopedia.play.broadcaster.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.*
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_UNKNOWN
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.ui.bottomsheet.WarningInfoBottomSheet.WarningType
import com.tokopedia.content.common.ui.model.AccountStateInfo
import com.tokopedia.content.common.ui.model.AccountStateInfoType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.socket.PlayBroadcastWebSocketMapper
import com.tokopedia.play.broadcaster.data.type.PlaySocketType
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.model.socket.PinnedMessageSocketResponse
import com.tokopedia.play.broadcaster.domain.model.socket.SectionedProductTagSocketResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.pusher.*
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimerState
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizChoiceDetailStateUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizDetailStateUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormDataUiModel
import com.tokopedia.play.broadcaster.ui.model.game.quiz.QuizFormStateUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.interactive.InteractiveSetupUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.ui.state.*
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.removeUnusedField
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.setupAutoAddField
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.setupEditable
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.trim
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.updateQuizOptionFlow
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.LeadeboardType
import com.tokopedia.play_common.model.ui.LeaderboardGameUiModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.play_common.util.extension.combine
import com.tokopedia.play_common.util.extension.setValue
import com.tokopedia.play_common.util.extension.switch
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketClosedReason
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.getAndUpdate
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastViewModel @AssistedInject constructor(
    @Assisted private val handle: SavedStateHandle,
    private val mDataStore: PlayBroadcastDataStore,
    private val hydraConfigStore: HydraConfigStore,
    private val sharedPref: HydraSharedPreferences,
    private val getChannelUseCase: GetChannelUseCase,
    private val getAddedChannelTagsUseCase: GetAddedChannelTagsUseCase,
    private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val playBroadcastWebSocket: PlayWebSocket,
    private val playBroadcastMapper: PlayBroadcastMapper,
    private val productMapper: PlayBroProductUiMapper,
    private val interactiveMapper: PlayInteractiveMapper,
    private val repo: PlayBroadcastRepository,
    private val logger: PlayLogger,
    private val broadcastTimer: PlayBroadcastTimer,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(handle: SavedStateHandle): PlayBroadcastViewModel
    }

    val channelId: String
        get() = hydraConfigStore.getChannelId()
    val channelTitle: String
        get() {
            return when (val titleModel = mDataStore.getSetupDataStore().getTitle()) {
                is PlayTitleUiModel.HasTitle -> titleModel.title
                else -> ""
            }
        }

    val remainingDurationInMillis: Long
        get() = broadcastTimer.remainingDuration

    val isUserLoggedIn: Boolean
        get() = userSession.isLoggedIn

    val observableConfigInfo: LiveData<NetworkResult<ConfigurationUiModel>>
        get() = _observableConfigInfo
    val observableTotalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val observableTotalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableNewMetrics: LiveData<Event<List<PlayMetricUiModel>>>
        get() = _observableNewMetrics
    val observableCover = getCurrentSetupDataStore().getObservableSelectedCover()
    val observableTitle: LiveData<PlayTitleUiModel.HasTitle> =
        getCurrentSetupDataStore().getObservableTitle()
            .filterIsInstance<PlayTitleUiModel.HasTitle>()
            .asLiveData(viewModelScope.coroutineContext + dispatcher.computation)
    val observableEvent: LiveData<EventUiModel>
        get() = _observableEvent
    val observableLeaderboardInfo: LiveData<NetworkResult<List<LeaderboardGameUiModel>>>
        get() = _observableLeaderboardInfo
    val shareContents: String
        get() = _observableShareInfo.value.orEmpty()
    val interactiveId: String
        get() = getCurrentSetupDataStore().getInteractiveId()
    val activeInteractiveTitle: String
        get() = getCurrentSetupDataStore().getActiveInteractiveTitle()

    val productSectionList: List<ProductTagSectionUiModel>
        get() = _productSectionList.value

    private val _observableConfigInfo = MutableLiveData<NetworkResult<ConfigurationUiModel>>()
    private val _observableChannelInfo = MutableLiveData<NetworkResult<ChannelInfoUiModel>>()
    private val _observableTotalView = MutableLiveData<TotalViewUiModel>()
    private val _observableTotalLike = MutableLiveData<TotalLikeUiModel>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableNewMetrics = MutableLiveData<Event<List<PlayMetricUiModel>>>()
    private val _observableShareInfo = MutableLiveData<String>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observableEvent = MutableLiveData<EventUiModel>()
    private val _observableLeaderboardInfo =
        MutableLiveData<NetworkResult<List<LeaderboardGameUiModel>>>()

    private val _configInfo = MutableStateFlow<ConfigurationUiModel?>(null)
    private var isLiveStreamEnded = false

    private val _pinnedMessage = MutableStateFlow<PinnedMessageUiModel>(
        PinnedMessageUiModel.Empty()
    )
    private val _productSectionList = MutableStateFlow(emptyList<ProductTagSectionUiModel>())
    private val _isExiting = MutableStateFlow(false)
    private val _schedule = MutableStateFlow(ScheduleUiModel.Empty)

    private val _quizFormData = MutableStateFlow(QuizFormDataUiModel())
    private val _quizFormState =
        MutableStateFlow<QuizFormStateUiModel>(QuizFormStateUiModel.Nothing)
    private val _quizIsNeedToUpdateUI = MutableStateFlow(true)

    private val _interactive = MutableStateFlow<GameUiModel>(GameUiModel.Unknown)
    private val _interactiveConfig = MutableStateFlow(InteractiveConfigUiModel.empty())
    private val _interactiveSetup = MutableStateFlow(InteractiveSetupUiModel.Empty)

    private val _selectedAccount = MutableStateFlow(ContentAccountUiModel.Empty)

    private val _accountStateInfo = MutableStateFlow(AccountStateInfo())
    var warningInfoType: WarningType = WarningType.UNKNOWN
    var tncList: List<TermsAndConditionUiModel> = emptyList()

    private val _accountListState = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())

    private var isFirstOpen: Boolean = true

    val contentAccountList: List<ContentAccountUiModel>
        get() = _accountListState.value

    val isAllowChangeAccount: Boolean
        get() = if (GlobalConfig.isSellerApp()) false else _accountListState.value.size > 1

    val authorId: String
        get() = _selectedAccount.value.id

    val authorName: String
        get() = _selectedAccount.value.name

    private val authorType: String
        get() = _selectedAccount.value.type

    private val _channelUiState = _configInfo
        .filterNotNull()
        .map {
            PlayChannelUiState(
                canStream = it.streamAllowed,
                tnc = it.tnc,
            )
        }

    private val _quizDetailState =
        MutableStateFlow<QuizDetailStateUiModel>(QuizDetailStateUiModel.Empty)
    private val _quizChoiceDetailState =
        MutableStateFlow<QuizChoiceDetailStateUiModel>(QuizChoiceDetailStateUiModel.Empty)

    private val _onboarding = MutableStateFlow(OnboardingUiModel.create(sharedPref))

    private val _pinnedMessageUiState = _pinnedMessage.map {
        PinnedMessageUiState(
            message = if (it.isActive && !it.isInvalidId) it.message else "",
            editStatus = it.editStatus
        )
    }

    @Suppress("MagicNumber")
    private val _quizFormUiState = combine(
        _quizFormData, _quizFormState, _quizIsNeedToUpdateUI,
    ) { quizFormData, quizFormState, quizIsNeedToUpdateUI ->
        QuizFormUiState(
            quizFormData = quizFormData,
            quizFormState = quizFormState,
            isNeedToUpdateUI = quizIsNeedToUpdateUI,
        )
    }

    private val _quizBottomSheetUiState = combine(
        _quizDetailState, _quizChoiceDetailState
    ) { quizDetailState, quizChoiceDetailState ->
        QuizBottomSheetUiState(
            quizDetailState = quizDetailState,
            quizChoiceDetailState = quizChoiceDetailState,
        )
    }

    val uiState = combine(
        _channelUiState.distinctUntilChanged(),
        _pinnedMessageUiState.distinctUntilChanged(),
        _productSectionList,
        _schedule,
        _isExiting,
        _quizFormUiState,
        _interactive,
        _interactiveConfig,
        _interactiveSetup,
        _quizDetailState,
        _onboarding,
        _quizBottomSheetUiState,
        _selectedAccount,
        _accountStateInfo,
    ) { channelState,
        pinnedMessage,
        productMap,
        schedule,
        isExiting,
        quizForm,
        interactive,
        interactiveConfig,
        interactiveSetup,
        quizDetail,
        onBoarding,
        quizBottomSheetUiState,
        selectedFeedAccount,
        accountStateInfo ->
        PlayBroadcastUiState(
            channel = channelState,
            pinnedMessage = pinnedMessage,
            selectedProduct = productMap,
            schedule = schedule,
            isExiting = isExiting,
            quizForm = quizForm,
            game = interactive,
            interactiveConfig = interactiveConfig,
            interactiveSetup = interactiveSetup,
            quizDetail = quizDetail,
            onBoarding = onBoarding,
            quizBottomSheetUiState = quizBottomSheetUiState,
            selectedContentAccount = selectedFeedAccount,
            accountStateInfo = accountStateInfo
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(UI_STATE_STOP_TIMEOUT),
        PlayBroadcastUiState.Empty,
    )

    private val _uiEvent = MutableSharedFlow<PlayBroadcastEvent>(extraBufferCapacity = 100)
    val uiEvent: Flow<PlayBroadcastEvent>
        get() = _uiEvent

    val broadcastTimerStateChanged: Flow<PlayBroadcastTimerState>
        get() = broadcastTimer.stateChanged

    val isBroadcastStopped
        get() = mIsBroadcastStopped

    private var socketJob: Job? = null

    private var mIsBroadcastStopped = false

    private val gson by lazy { Gson() }

    init {
        val savedTitle = handle.get<String>(KEY_TITLE)
        if (savedTitle != null) getCurrentSetupDataStore().setTitle(savedTitle)

        viewModelScope.launch(dispatcher.computation) {
            getCurrentSetupDataStore().getObservableTitle().collectLatest {
                if (it is PlayTitleUiModel.HasTitle) handle[KEY_TITLE] = it.title
                else handle.remove(KEY_TITLE)
            }
        }

        _observableChatList.value = mutableListOf()
    }

    fun saveState(outState: Bundle) {
        outState.putParcelable(KEY_CONFIG, _configInfo.value)
        outState.putBoolean(KEY_IS_LIVE_STREAM_ENDED, isLiveStreamEnded)
        outState.putParcelable(KEY_AUTHOR, hydraConfigStore.getAuthor())
    }

    fun restoreState(savedInstanceState: Bundle) {
        _configInfo.value = savedInstanceState.getParcelable(KEY_CONFIG)
        isLiveStreamEnded = savedInstanceState.getBoolean(KEY_IS_LIVE_STREAM_ENDED)
        hydraConfigStore.setAuthor(savedInstanceState.getParcelable(KEY_AUTHOR) ?: ContentAccountUiModel.Empty)
    }

    fun setIsLiveStreamEnded() {
        isLiveStreamEnded = true
    }

    fun isLiveStreamEnded() = isLiveStreamEnded

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun submitAction(event: PlayBroadcastAction) {
        when (event) {
            PlayBroadcastAction.EditPinnedMessage -> handleEditPinnedMessage()
            is PlayBroadcastAction.SetPinnedMessage -> handleSetPinnedMessage(event.message)
            PlayBroadcastAction.CancelEditPinnedMessage -> handleCancelEditPinnedMessage()
            is PlayBroadcastAction.SetCover -> handleSetCover(event.cover)
            is PlayBroadcastAction.SetProduct -> handleSetProduct(event.productTagSectionList)
            is PlayBroadcastAction.SetSchedule -> handleSetSchedule(event.date)
            PlayBroadcastAction.DeleteSchedule -> handleDeleteSchedule()
            is PlayBroadcastAction.GetAccountList -> handleGetAccountList(event.selectedType)
            is PlayBroadcastAction.SwitchAccount -> handleSwitchAccount()

            /** Game */
            is PlayBroadcastAction.ClickGameOption -> handleClickGameOption(event.gameType)

            /** Quiz */
            PlayBroadcastAction.ClickBackOnQuiz -> handleClickBackOnQuiz()
            PlayBroadcastAction.ClickNextOnQuiz -> handleClickNextOnQuiz()
            is PlayBroadcastAction.InputQuizTitle -> handleInputQuizTitle(event.title)
            is PlayBroadcastAction.InputQuizOption -> handleInputQuizOption(event.order, event.text)
            is PlayBroadcastAction.SelectQuizOption -> handleSelectQuizOption(event.order)
            is PlayBroadcastAction.SaveQuizData -> handleSaveQuizData(event.quizFormData)
            is PlayBroadcastAction.SelectQuizDuration -> handleSelectQuizDuration(event.duration)
            PlayBroadcastAction.SubmitQuizForm -> handleSubmitQuizForm()
            PlayBroadcastAction.QuizEnded -> handleQuizEnded()
            is PlayBroadcastAction.ClickQuizChoiceOption -> handleChoiceDetail(event.choice)
            PlayBroadcastAction.LoadMoreCurrentChoiceParticipant -> handleLoadMoreParticipant()
            PlayBroadcastAction.ClickGameResultWidget -> handleClickGameResultWidget()
            /**
             * Giveaway
             */
            PlayBroadcastAction.GiveawayUpcomingEnded -> handleGiveawayUpcomingEnded()
            PlayBroadcastAction.GiveawayOngoingEnded -> handleGiveawayOngoingEnded()
            is PlayBroadcastAction.CreateGiveaway -> handleCreateGiveaway(
                event.title, event.durationInMs
            )
            is PlayBroadcastAction.ClickOngoingWidget -> handleClickOngoingWidget()
            PlayBroadcastAction.ClickBackOnChoiceDetail -> handleBackClickOnChoiceDetail()
            PlayBroadcastAction.DismissQuizDetailBottomSheet -> handleCloseQuizDetailBottomSheet()
            PlayBroadcastAction.ClickRefreshQuizDetailBottomSheet -> handleRefreshQuizDetail()
            PlayBroadcastAction.ClickRefreshQuizOption -> handleRefreshQuizOptionDetail()
            is PlayBroadcastAction.ClickPinProduct -> handleClickPin(event.product)
            is PlayBroadcastAction.BroadcastStateChanged -> handleBroadcastStateChanged(event.state)
        }
    }

    fun getCurrentSetupDataStore(): PlayBroadcastSetupDataStore {
        return mDataStore.getSetupDataStore()
    }

    private fun getConfiguration(selectedAccount: ContentAccountUiModel) {
        viewModelScope.launchCatchError(block = {

            val currConfigInfo = _configInfo.value
            val configUiModel = repo.getChannelConfiguration(selectedAccount.id, selectedAccount.type)
            setChannelId(configUiModel.channelId)
            _configInfo.value = configUiModel

            if (!isAccountEligible(configUiModel, selectedAccount)) {
                if (isFirstOpen && isAllowChangeAccount) {
                    isFirstOpen = false
                    handleSwitchAccount(false)
                }
                if (currConfigInfo != null) {
                    setChannelId(currConfigInfo.channelId)
                    _configInfo.value = currConfigInfo
                }
                _observableConfigInfo.value = NetworkResult.Success(currConfigInfo ?: configUiModel)
                return@launchCatchError
            }

            isFirstOpen = false

            // create channel when there are no channel exist
            if (configUiModel.channelStatus == ChannelStatus.Unknown) createChannel()

            // get channel when channel status is paused
            if (configUiModel.channelStatus == ChannelStatus.Pause
                // also when complete draft is true
                || configUiModel.channelStatus == ChannelStatus.CompleteDraft
                || configUiModel.channelStatus == ChannelStatus.Draft
            ) {
                val deferredChannel = asyncCatchError(block = {
                    getChannelById(configUiModel.channelId)
                }) { it }
                val deferredProductMap = asyncCatchError(block = {
                    repo.getProductTagSummarySection(channelID = configUiModel.channelId)
                }) { emptyList() }

                val error = deferredChannel.await()
                val productMap = deferredProductMap.await()

                if (error != null) throw error
                setSelectedProduct(productMap.orEmpty())
            }

            setProductConfig(configUiModel.productTagConfig)
            setCoverConfig(configUiModel.coverConfig)
            setDurationConfig(configUiModel.durationConfig)
            setScheduleConfig(configUiModel.scheduleConfig)

            broadcastTimer.setupDuration(
                configUiModel.durationConfig.remainingDuration,
                configUiModel.durationConfig.maxDuration
            )
            broadcastTimer.setupPauseDuration(
                configUiModel.durationConfig.pauseDuration
            )

            updateSelectedAccount(selectedAccount)
            _observableConfigInfo.value = NetworkResult.Success(configUiModel)
        }) {
            _observableConfigInfo.value = NetworkResult.Fail(it) { getConfiguration(selectedAccount) }
        }
    }

    private suspend fun createChannel() {
        val channelId = repo.createChannel(authorId, authorType)
        setChannelId(channelId)
    }

    private suspend fun getChannelById(channelId: String): Throwable? {
        return try {
            val (channel, tags) = supervisorScope {
                val channelDeferred = async(dispatcher.io) {
                    getChannelUseCase.apply {
                        params = GetChannelUseCase.createParams(channelId)
                    }.executeOnBackground()
                }

                val tagsDeferred = async {
                    getAddedChannelTagsUseCase.apply {
                        setChannelId(channelId)
                    }.executeOnBackground()
                }

                return@supervisorScope channelDeferred.await() to tagsDeferred.await()
            }

            val channelInfo = playBroadcastMapper.mapChannelInfo(channel)
            _observableChannelInfo.value = NetworkResult.Success(channelInfo)

            logChannelStatus(channelInfo.status)

            setChannelId(channelInfo.channelId)
            setChannelTitle(channelInfo.title)
            setChannelInfo(channelInfo)

            setAddedTags(tags.recommendedTags.tags.toSet())

            setSelectedCover(
                playBroadcastMapper.mapCover(
                    getCurrentSetupDataStore().getSelectedCover(),
                    channel.basic.coverUrl
                )
            )
            setBroadcastSchedule(playBroadcastMapper.mapChannelSchedule(channel.basic.timestamp, channel.basic.status))

            generateShareLink(playBroadcastMapper.mapShareInfo(channel))
            null
        } catch (err: Throwable) {
            _observableChannelInfo.value = NetworkResult.Fail(err)
            err
        }
    }

    private suspend fun updateChannelStatus(status: PlayChannelStatusType): String {
        return withContext(dispatcher.io) {
            repo.updateChannelStatus(authorId, channelId, status)
        }
    }

    fun setChannelId(channelId: String) {
        hydraConfigStore.setChannelId(channelId)
    }

    fun sendLogs() {
        logger.sendAll(channelId)
    }

    private fun setActiveInteractiveTitle(title: String) {
        getCurrentSetupDataStore().setActiveInteractiveTitle(title)
    }

    private fun isCreateSessionAllowed(duration: Long): Boolean {
        val delayGqlDuration = INTERACTIVE_GQL_CREATE_DELAY
        return broadcastTimer.remainingDuration > duration + delayGqlDuration
    }

    private fun getInteractiveConfig() {
        viewModelScope.launchCatchError(block = {
            val gameConfig = repo.getInteractiveConfig(authorId, authorType)

            _interactiveConfig.value = gameConfig

            if (!gameConfig.isNoGameActive()) {
                initQuizFormData()
                handleActiveInteractive()
            }
        }) { }
    }

    private suspend fun updateCurrentInteractiveStatus() {
        val interactiveConfig = _interactiveConfig.value
        if (!interactiveConfig.isNoGameActive()) handleActiveInteractive()
    }

    private suspend fun handleActiveInteractive() {
        try {
            val currentInteractive = repo.getCurrentInteractive(channelId)
            handleActiveInteractiveFromNetwork(currentInteractive)
        } catch (e: Throwable) {
            _interactive.value = GameUiModel.Unknown
        }
    }

    private fun handleActiveInteractiveFromNetwork(game: GameUiModel) {
        setInteractiveId(game.id)
        setActiveInteractiveTitle(game.title)
        when (game) {
            is GameUiModel.Giveaway -> setupGiveaway(game)
            is GameUiModel.Quiz -> setupQuiz(game)
            else -> {}
        }
    }

    private fun setupGiveaway(giveaway: GameUiModel.Giveaway) {
        _interactive.value = giveaway
        when (giveaway.status) {
            GameUiModel.Giveaway.Status.Finished -> displayGameResultWidgetIfHasLeaderBoard()
            GameUiModel.Giveaway.Status.Unknown -> stopInteractive()
        }
    }

    private fun setupQuiz(quiz: GameUiModel.Quiz) {
        _interactive.value = quiz
        when (quiz.status) {
            GameUiModel.Quiz.Status.Finished -> displayGameResultWidgetIfHasLeaderBoard()
            GameUiModel.Quiz.Status.Unknown -> stopInteractive()
        }
    }

    private suspend fun getSocketCredential(): GetSocketCredentialResponse.SocketCredential = try {
        withContext(dispatcher.io) {
            return@withContext getSocketCredentialUseCase.executeOnBackground()
        }
    } catch (e: Throwable) {
        GetSocketCredentialResponse.SocketCredential()
    }

    private fun startWebSocket() {
        socketJob?.cancel()
        socketJob = viewModelScope.launch {
            val socketCredential = getSocketCredential()

            if (!isActive) return@launch
            connectWebSocket(
                channelId = channelId,
                socketCredential = socketCredential
            )

            playBroadcastWebSocket.listenAsFlow()
                .collect {
                    handleWebSocketResponse(it, channelId, socketCredential)
                }
        }
    }

    /**
     * In bro, warehouse Id is always 0 (OOC) dunno later
     */
    private fun connectWebSocket(channelId: String, socketCredential: GetSocketCredentialResponse.SocketCredential) {
        playBroadcastWebSocket.connect(channelId, "0", socketCredential.gcToken, WEB_SOCKET_SOURCE_PLAY_BROADCASTER)
    }

    private fun closeWebSocket() {
        playBroadcastWebSocket.close()
        socketJob?.cancel()
    }

    private suspend fun handleWebSocketResponse(
        response: WebSocketAction,
        channelId: String,
        socketCredential: GetSocketCredentialResponse.SocketCredential
    ) {
        when (response) {
            is WebSocketAction.NewMessage -> handleWebSocketMessage(response.message)
            is WebSocketAction.Closed -> if (response.reason is WebSocketClosedReason.Error) connectWebSocket(
                channelId,
                socketCredential
            )
        }
    }

    private suspend fun handleWebSocketMessage(message: WebSocketResponse) {
        val result = withContext(dispatcher.computation) {
            val socketMapper = PlayBroadcastWebSocketMapper(message, gson)
            socketMapper.map()
        }
        when (result) {
            is NewMetricList -> queueNewMetrics(playBroadcastMapper.mapNewMetricList(result))
            is TotalView -> _observableTotalView.value = playBroadcastMapper.mapTotalView(result)
            is TotalLike -> _observableTotalLike.value = playBroadcastMapper.mapTotalLike(result)
            is LiveDuration -> {
                restartLiveDuration(result)
                if (result.duration >= result.maxDuration) logSocket(result)
            }
            is SectionedProductTagSocketResponse -> {
                setSelectedProduct(productMapper.mapSectionedProduct(result))
            }
            is Chat -> retrieveNewChat(playBroadcastMapper.mapIncomingChat(result))
            is Freeze -> {
                val eventUiModel =
                        playBroadcastMapper.mapFreezeEvent(result, _observableEvent.value)
                if (eventUiModel.freeze) {
                    _observableEvent.value = eventUiModel
                    logSocket(result)
                }
            }
            is Banned -> {
                val eventUiModel =
                        playBroadcastMapper.mapBannedEvent(result, _observableEvent.value)
                if (eventUiModel.banned) {
                    _observableEvent.value = eventUiModel
                    logSocket(result)
                }
            }
            is GiveawayResponse -> {
                val currentInteractive = interactiveMapper.mapGiveaway(
                    result,
                    TimeUnit.SECONDS.toMillis(result.waitingDuration.toLong())
                )
                handleActiveInteractiveFromNetwork(currentInteractive)
            }
            is QuizResponse -> {
                val currentInteractive = interactiveMapper.mapQuiz(
                    result,
                    TimeUnit.SECONDS.toMillis(result.waitingDuration)
                )
                handleActiveInteractiveFromNetwork(currentInteractive)
            }
            is PinnedMessageSocketResponse -> {
                val mappedResult = playBroadcastMapper.mapPinnedMessageSocket(result)
                _pinnedMessage.value = mappedResult.copy(
                    editStatus = _pinnedMessage.value.editStatus
                )
            }
        }
    }

    private fun setSelectedProduct(productSectionList: List<ProductTagSectionUiModel>) {
        _productSectionList.value = productSectionList
    }

    private fun setSelectedCover(cover: PlayCoverUiModel) {
        getCurrentSetupDataStore().setFullCover(cover)
    }

    private fun setChannelTitle(title: String) {
        getCurrentSetupDataStore().setTitle(title)
    }

    private fun setBroadcastSchedule(schedule: BroadcastScheduleUiModel) {
        _schedule.update {
            it.copy(schedule = schedule)
        }
    }

    private fun setChannelInfo(channelInfo: ChannelInfoUiModel) {
        hydraConfigStore.setIngestUrl(channelInfo.ingestUrl)
    }

    private fun setAddedTags(tags: Set<String>) {
        getCurrentSetupDataStore().setTags(tags)
    }

    private fun setProductConfig(configModel: ProductTagConfigUiModel) {
        hydraConfigStore.setMaxProduct(configModel.maxProduct)
        hydraConfigStore.setMinProduct(configModel.minProduct)
        hydraConfigStore.setMaxProductDesc(configModel.maxProductDesc)
    }

    private fun setCoverConfig(configModel: CoverConfigUiModel) {
        hydraConfigStore.setMaxTitleChars(configModel.maxChars)
    }

    private fun setDurationConfig(configModel: DurationConfigUiModel) {
        hydraConfigStore.setMaxDurationDesc(configModel.maxDurationDesc)
    }

    private fun setScheduleConfig(scheduleConfigModel: BroadcastScheduleConfigUiModel) {
        hydraConfigStore.setMinScheduleDate(scheduleConfigModel.minimum)
        hydraConfigStore.setMaxScheduleDate(scheduleConfigModel.maximum)
        hydraConfigStore.setDefaultScheduleDate(scheduleConfigModel.default)

        _schedule.update {
            it.copy(
                config = ScheduleConfigUiModel(
                    maxDate = scheduleConfigModel.maximum,
                    minDate = scheduleConfigModel.minimum,
                    defaultDate = scheduleConfigModel.default,
                ),
                canSchedule = repo.canSchedule()
            )
        }
    }

    private fun restartLiveDuration(duration: LiveDuration) {
        viewModelScope.launchCatchError(block = {
            val durationInMillis = TimeUnit.SECONDS.toMillis(duration.duration)
            broadcastTimer.restart(durationInMillis)
        }) { }
    }

    private fun setInteractiveId(id: String) {
        getCurrentSetupDataStore().setInteractiveId(id)
    }

    private fun getPinnedMessage() {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            val activePinned = repo.getActivePinnedMessage(channelId)
            _pinnedMessage.value = activePinned ?: PinnedMessageUiModel.Empty()
        }) {}
    }

    fun getQuizDetailData() {
        _quizDetailState.value = QuizDetailStateUiModel.Loading
        viewModelScope.launchCatchError(block = {
            val quizDetailUiModel = repo.getInteractiveQuizDetail(interactiveId)
            _quizDetailState.value = QuizDetailStateUiModel.Success(
                    playBroadcastMapper.mapQuizDetailToLeaderBoard(quizDetailUiModel, endTimeInteractive)
            )
        }) {
            _quizDetailState.value =
                QuizDetailStateUiModel.Error(allowChat = false, isQuizDetail = true)
        }
    }

    private fun getQuizChoiceDetailData(
        choiceId: String,
        index: Int,
        cursor: String = "",
        interactiveId: String,
        interactiveTitle: String,
    ) {
        val oldParticipant = when (val state = _quizChoiceDetailState.value) {
            is QuizChoiceDetailStateUiModel.Success -> {
                if (cursor.isNotBlank()) state.dataUiModel.participants else emptyList()
            }
            else -> emptyList()
        }
        _quizChoiceDetailState.value = QuizChoiceDetailStateUiModel.Loading
        viewModelScope.launchCatchError(block = {
            val quizChoicesDetailUiModel = repo.getInteractiveQuizChoiceDetail(
                choiceIndex = index,
                choiceId = choiceId,
                cursor = cursor,
                interactiveId = interactiveId,
                interactiveTitle = interactiveTitle
            )
            if (cursor.isNotBlank()) {
                val updatedQuizChoicesDetailUiModel =
                    quizChoicesDetailUiModel.copy(participants = oldParticipant + quizChoicesDetailUiModel.participants)
                _quizChoiceDetailState.value =
                    QuizChoiceDetailStateUiModel.Success(updatedQuizChoicesDetailUiModel)
            } else {
                _quizChoiceDetailState.value =
                    QuizChoiceDetailStateUiModel.Success(quizChoicesDetailUiModel)
            }
        }) {
            _quizChoiceDetailState.value = QuizChoiceDetailStateUiModel.Error(
                choiceId,
                index,
                interactiveId,
                interactiveTitle,
            )
        }
    }

    fun getLeaderboardWithSlots(allowChat: Boolean = false) {
        _quizDetailState.value = QuizDetailStateUiModel.Loading
        viewModelScope.launchCatchError(block = {
            val leaderboardSlots = repo.getSellerLeaderboardWithSlot(channelId, allowChat).map {
                if(it is LeaderboardGameUiModel.Header && it.leaderBoardType == LeadeboardType.Quiz && (_interactive.value as? GameUiModel.Quiz)?.status is GameUiModel.Quiz.Status.Ongoing && it.id == _interactive.value.id) it.copy(endsIn = endTimeInteractive)
                else it
            }
            _quizDetailState.value = QuizDetailStateUiModel.Success(leaderboardSlots)
        }) {
            _quizDetailState.value =
                QuizDetailStateUiModel.Error(allowChat = allowChat, isQuizDetail = false)
        }
    }

    private val endTimeInteractive: Calendar? get() {
        return when (val interactive = _interactive.value){
            is GameUiModel.Quiz -> return when (val status = interactive.status){
                is GameUiModel.Quiz.Status.Ongoing -> {
                    status.endTime
                }
                else -> null
            }
            else -> null
        }
    }

    private fun displayGameResultWidgetIfHasLeaderBoard() {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            val leaderboardSlots = repo.getSellerLeaderboardWithSlot(channelId, false).map {
                if(it is LeaderboardGameUiModel.Header && it.leaderBoardType == LeadeboardType.Quiz && it.id == _interactive.value.id) it.copy(endsIn = endTimeInteractive)
                else it
            }
            if (leaderboardSlots.isNotEmpty()) {
                _uiEvent.emit(PlayBroadcastEvent.ShowInteractiveGameResultWidget(sharedPref.isFirstGameResult()))
                sharedPref.setNotFirstGameResult()
                delay(DEFAULT_GAME_RESULT_COACHMARK_AUTO_DISMISS)
                _uiEvent.emit(PlayBroadcastEvent.DismissGameResultCoachMark)
            }
        }) {
            it.printStackTrace()
        }
    }

    private fun handleEditPinnedMessage() {
        _pinnedMessage.setValue {
            copy(editStatus = PinnedMessageEditStatus.Editing)
        }
    }

    private fun handleSetPinnedMessage(message: String) {
        _pinnedMessage.setValue {
            copy(editStatus = PinnedMessageEditStatus.Uploading)
        }

        viewModelScope.launchCatchError(dispatcher.io, block = {
            val pinnedMessage = _pinnedMessage.value
            _pinnedMessage.value = repo.setPinnedMessage(
                id = if (pinnedMessage.isInvalidId) null else pinnedMessage.id,
                channelId = channelId,
                message = message
            )
        }) {
            _pinnedMessage.setValue {
                copy(editStatus = PinnedMessageEditStatus.Editing)
            }
            _uiEvent.emit(PlayBroadcastEvent.ShowError(it))
        }
    }

    private fun handleCancelEditPinnedMessage() {
        if (_pinnedMessage.value.editStatus == PinnedMessageEditStatus.Uploading) return
        _pinnedMessage.setValue {
            copy(editStatus = PinnedMessageEditStatus.Nothing)
        }
    }

    private fun handleSetCover(cover: PlayCoverUiModel) {
        setSelectedCover(cover)
    }

    private fun handleSetProduct(productSectionList: List<ProductTagSectionUiModel>) {
        setSelectedProduct(productSectionList)
    }

    private fun handleSetSchedule(selectedDate: Date) {
        _schedule.update {
            it.copy(state = NetworkState.Loading)
        }
        viewModelScope.launchCatchError(dispatcher.io, block = {
            val isEdit = _schedule.value.schedule != BroadcastScheduleUiModel.NoSchedule
            _schedule.update {
                it.copy(
                    schedule = repo.updateSchedule(channelId, selectedDate),
                    state = NetworkState.Success,
                )
            }
            _uiEvent.emit(
                PlayBroadcastEvent.SetScheduleSuccess(isEdit = isEdit)
            )
        }) { err ->
            _schedule.update {
                it.copy(state = NetworkState.Failed)
            }
            _uiEvent.emit(PlayBroadcastEvent.ShowScheduleError(err))
        }
    }

    private fun handleDeleteSchedule() {
        _schedule.update {
            it.copy(state = NetworkState.Loading)
        }
        viewModelScope.launchCatchError(dispatcher.io, block = {
            _schedule.update {
                it.copy(
                    schedule = repo.updateSchedule(channelId, null),
                    state = NetworkState.Success,
                )
            }
            _uiEvent.emit(PlayBroadcastEvent.DeleteScheduleSuccess)
        }) { err ->
            _schedule.update {
                it.copy(state = NetworkState.Failed)
            }
            _uiEvent.emit(PlayBroadcastEvent.ShowScheduleError(err))
        }
    }

    /** Handle Game Action */
    private fun handleClickGameOption(gameType: GameType) {
        when (gameType) {
            GameType.Giveaway -> {
                _interactiveSetup.update {
                    it.copy(type = gameType)
                }
            }
            GameType.Quiz -> {
                /**
                 * TODO("Quiz to also use [_interactiveSetup], because only 1 game can be active at a time")
                 */
                _quizFormState.setValue { next() }
            }
            else -> {
                _interactiveSetup.update {
                    it.copy(type = GameType.Unknown)
                }
            }
        }
    }

    private fun handleClickBackOnQuiz() {
        needUpdateQuizForm(true) {
            _quizFormState.setValue { prev() }
            updateOptionsState()
        }
    }

    private fun handleClickNextOnQuiz() {
        needUpdateQuizForm(true) {
            _quizFormState.setValue { next() }
            updateOptionsState()

            if (_quizFormState.value is QuizFormStateUiModel.SetDuration) {
                updateQuizEligibleDuration()
            }
        }
    }

    private fun handleInputQuizTitle(title: String) {
        needUpdateQuizForm(false) {
            _quizFormData.setValue { copy(title = title) }
        }
    }

    private fun handleInputQuizOption(order: Int, newText: String) {
        val options = _quizFormData.value.options
        val quizConfig = _interactiveConfig.value.quizConfig

        val (newOptions, needUpdate) = options.updateQuizOptionFlow(
            order,
            newText,
            quizConfig,
            sharedPref.isFirstSelectQuizOption()
        )

        sharedPref.setNotFirstSelectQuizOption()

        needUpdateQuizForm(needUpdate) {
            _quizFormData.setValue { copy(options = newOptions) }
        }
    }

    private fun handleSelectQuizOption(order: Int) {
        val options = _quizFormData.value.options

        val currSelectedOrder = options.firstOrNull { it.isSelected }?.order ?: -1

        if (currSelectedOrder == order || currSelectedOrder == -1) return

        sharedPref.setNotFirstSelectQuizOption()

        needUpdateQuizForm(true) {
            _quizFormData.setValue {
                copy(options = options.map {
                    it.copy(
                        isSelected = it.order == order,
                        isFocus = it.order == order,
                    )
                })
            }
        }
    }

    private fun handleSaveQuizData(quizFormData: QuizFormDataUiModel) {
        needUpdateQuizForm(false) {
            _quizFormData.setValue { quizFormData }
        }
    }

    private fun handleSelectQuizDuration(duration: Long) {
        needUpdateQuizForm(false) {
            updateQuizEligibleDuration()
            _quizFormData.setValue { copy(durationInMs = duration) }
        }
    }

    private fun handleSubmitQuizForm() {
        viewModelScope.launchCatchError(block = {
            _quizFormState.setValue { QuizFormStateUiModel.SetDuration(true) }
            val quizData = _quizFormData.value
            val durationInSecond = TimeUnit.MILLISECONDS.toSeconds(quizData.durationInMs)
            repo.createInteractiveQuiz(
                channelId = channelId,
                question = quizData.title,
                runningTime = durationInSecond,
                choices = quizData.options.map { playBroadcastMapper.mapQuizOptionToChoice(it) },
            )

            handleActiveInteractive()

            /** Reset Form */
            sharedPref.setNotFirstSelectQuizOption()
            sharedPref.setNotFirstInteractive()
            _onboarding.update {
                it.copy(firstInteractive = sharedPref.isFirstInteractive())
            }
            initQuizFormData()
            _quizFormState.setValue { QuizFormStateUiModel.Nothing }
        }) {
            _quizFormState.setValue { QuizFormStateUiModel.SetDuration(false) }
            _uiEvent.emit(PlayBroadcastEvent.ShowErrorCreateQuiz(it))
        }
    }

    /**
     * When pre-start finished, game should be played (e.g. TapTap)
     */
    private fun handleGiveawayUpcomingEnded() {
        viewModelScope.launchCatchError(dispatcher.computation, block = {
            _interactive.update {
                val currentGiveaway =
                    it as? GameUiModel.Giveaway ?: error("Interactive is not giveaway")
                val upcomingStatus =
                    currentGiveaway.status as? GameUiModel.Giveaway.Status.Upcoming
                        ?: error("Giveaway status is not upcoming")

                val interactive = it.copy(
                    status = GameUiModel.Giveaway.Status.Ongoing(
                        endTime = upcomingStatus.endTime
                    )
                )
                interactive
            }

        }) {
            _interactive.value = GameUiModel.Unknown
        }
    }

    private fun handleGiveawayOngoingEnded() {
        viewModelScope.launchCatchError(dispatcher.computation, block = {
            val interactive = _interactive.getAndUpdate {
                if (it !is GameUiModel.Giveaway) error("Interactive is not giveaway")
                val newInteractive = it.copy(
                    status = GameUiModel.Giveaway.Status.Finished
                )
                newInteractive
            }
            setActiveInteractiveTitle("")
            setInteractiveId("")
            if (interactive.waitingDuration > 0)
                delay(interactive.waitingDuration)
            else
                delay(INTERACTIVE_GQL_LEADERBOARD_DELAY)
            displayGameResultWidgetIfHasLeaderBoard()
            _interactive.value = GameUiModel.Unknown
        }) {
            _interactive.value = GameUiModel.Unknown
        }
    }

    private fun handleQuizEnded() {
        viewModelScope.launchCatchError(dispatcher.computation, block = {
            val interactive = _interactive.getAndUpdate {
                if (it !is GameUiModel.Quiz) error("Interactive is not quiz")
                val interactive = it.copy(
                    status = GameUiModel.Quiz.Status.Finished
                )
                interactive
            }
            setActiveInteractiveTitle("")
            setInteractiveId("")
            if (interactive.waitingDuration > 0)
                delay(interactive.waitingDuration)
            else
                delay(INTERACTIVE_GQL_LEADERBOARD_DELAY)
            displayGameResultWidgetIfHasLeaderBoard()
            _interactive.value = GameUiModel.Unknown
        }) {
            _interactive.value = GameUiModel.Unknown
        }
    }

    private fun stopInteractive(){
        setActiveInteractiveTitle("")
        setInteractiveId("")
        displayGameResultWidgetIfHasLeaderBoard()
        _interactive.value = GameUiModel.Unknown
    }

    private fun handleChoiceDetail(choice: QuizChoicesUiModel) {
        getQuizChoiceDetailData(
            choiceId = choice.id,
            index = choice.index,
            interactiveId = choice.interactiveId,
            interactiveTitle = choice.interactiveTitle
        )
    }

    private fun handleLoadMoreParticipant() {
        when (val state = _quizChoiceDetailState.value) {
            is QuizChoiceDetailStateUiModel.Success -> {
                if (state.dataUiModel.cursor != FLAG_END_CURSOR) {
                    getQuizChoiceDetailData(
                        choiceId = state.dataUiModel.choice.id,
                        index = state.dataUiModel.choice.index,
                        cursor = state.dataUiModel.cursor,
                        interactiveId = state.dataUiModel.choice.interactiveId,
                        interactiveTitle = state.dataUiModel.choice.interactiveTitle,
                    )
                }
            }
        }
    }

    private fun handleCreateGiveaway(
        title: String,
        durationInMs: Long,
    ) {
        _interactiveSetup.update {
            it.copy(isSubmitting = true)
        }
        if (!isCreateSessionAllowed(durationInMs)) {
            _uiEvent.tryEmit(
                PlayBroadcastEvent.ShowError(
                    IllegalStateException("Not allowed to create session")
                )
            )
        }

        viewModelScope.launchCatchError(dispatcher.io, block = {
            val session = repo.createGiveaway(channelId, title, durationInMs)
            handleActiveInteractive()
            _interactiveSetup.update {
                it.copy(
                    type = GameType.Unknown,
                    isSubmitting = false,
                )
            }
            _uiEvent.emit(PlayBroadcastEvent.CreateInteractive.Success(session.durationInMs))

            sharedPref.setNotFirstInteractive()
            _onboarding.update {
                it.copy(firstInteractive = sharedPref.isFirstInteractive())
            }
        }) { err ->
            _interactiveSetup.update {
                it.copy(isSubmitting = false)
            }
            _uiEvent.emit(PlayBroadcastEvent.CreateInteractive.Error(err))
        }
    }

    private fun handleClickOngoingWidget() {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            if (uiState.value.game is GameUiModel.Quiz) {
                _uiEvent.emit(PlayBroadcastEvent.ShowQuizDetailBottomSheet)
            }
        }) { err ->
            _uiEvent.emit(PlayBroadcastEvent.ShowQuizDetailBottomSheetError(err))
        }
    }

    private fun handleClickGameResultWidget() {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            _uiEvent.emit(PlayBroadcastEvent.ShowLeaderboardBottomSheet)
        }) { err ->
            _uiEvent.emit(PlayBroadcastEvent.ShowLeaderboardBottomSheetError(err))
        }
    }

    private fun handleBackClickOnChoiceDetail() {
        _quizChoiceDetailState.value = QuizChoiceDetailStateUiModel.Empty
    }

    private fun handleCloseQuizDetailBottomSheet() {
        _quizChoiceDetailState.value = QuizChoiceDetailStateUiModel.Empty
        _quizDetailState.value = QuizDetailStateUiModel.Empty
    }

    private fun handleRefreshQuizDetail() {
        _quizChoiceDetailState.value = QuizChoiceDetailStateUiModel.Empty
        when (val state = _quizDetailState.value) {
            is QuizDetailStateUiModel.Error -> {
                if (state.isQuizDetail)
                    getQuizDetailData()
                else
                    getLeaderboardWithSlots(state.allowChat)
            }

        }
    }

    private fun handleRefreshQuizOptionDetail() {
        when (val state = _quizChoiceDetailState.value) {
            is QuizChoiceDetailStateUiModel.Error -> {
                getQuizChoiceDetailData(
                    choiceId = state.choiceId,
                    index = state.index,
                    interactiveId = state.interactiveId,
                    interactiveTitle = state.interactiveTitle,
                )
            }
        }
    }

    /**
     * Quiz Form
     */
    private fun initQuizFormData() {
        needUpdateQuizForm(true) {
            updateQuizEligibleDuration()

            val quizConfig = _interactiveConfig.value.quizConfig

            val initialOptions = List(quizConfig.minChoicesCount) {
                QuizFormDataUiModel.Option(
                    order = it,
                    isMandatory = true,
                )
            }
            val selectedInitialDurationIfFound =
                quizConfig.eligibleStartTimeInMs.indexOf(
                    TimeUnit.MINUTES.toMillis(
                        DEFAULT_QUIZ_DURATION_PICKER_IN_MINUTE
                    )
                ).coerceAtLeast(0)
            _quizFormData.update {
                QuizFormDataUiModel(
                    durationInMs = quizConfig.eligibleStartTimeInMs[selectedInitialDurationIfFound],
                    options = initialOptions
                )
            }
        }
    }

    private fun updateOptionsState() {
        val isStateEditable = isQuizStateEditable()

        val quizConfig = _interactiveConfig.value.quizConfig
        val quizFormData = _quizFormData.value

        val newTitle = if(!isStateEditable) quizFormData.title.trim() else quizFormData.title

        val options = quizFormData.options.toMutableList()
        val newOptions = if (isStateEditable) {
            options.setupAutoAddField(quizConfig)
        } else {
            options.removeUnusedField()
                    .trim()
        }.setupEditable(isStateEditable)

        _quizFormData.update {
            it.copy(
                title = newTitle,
                options = newOptions,
            )
        }
    }

    private fun isQuizStateEditable(): Boolean {
        return _quizFormState.value !is QuizFormStateUiModel.SetDuration
    }

    private fun updateQuizEligibleDuration() {
        val remainingDuration = broadcastTimer.remainingDuration
        val quizConfig = _interactiveConfig.value.quizConfig
        val newQuizConfig = quizConfig.copy(
            eligibleStartTimeInMs = quizConfig.availableStartTimeInMs.filter { it < remainingDuration }
        )

        _interactiveConfig.update {
            it.copy(quizConfig = newQuizConfig)
        }
    }

    private fun needUpdateQuizForm(isNeedUpdate: Boolean, block: () -> Unit) {
        _quizIsNeedToUpdateUI.value = false
        block()
        _quizIsNeedToUpdateUI.value = isNeedUpdate
    }

    private fun handleBroadcastStateChanged(state: PlayBroadcasterState) {
        logPusherState(state)
        when(state) {
            is PlayBroadcasterState.Started -> handleBroadcasterStart()
            is PlayBroadcasterState.Resume -> handleBroadcasterResume(state.startedBefore, state.shouldContinue)
            PlayBroadcasterState.Paused -> handleBroadcasterPause()
            is PlayBroadcasterState.Recovered -> handleBroadcasterRecovered()
            is PlayBroadcasterState.Error -> handleBroadcasterError(state.cause)
            PlayBroadcasterState.Stopped -> handleBroadcasterStop()
        }
    }

    private fun handleBroadcasterError(cause: Throwable) {
        viewModelScope.launch {
            _uiEvent.emit(PlayBroadcastEvent.ShowBroadcastError(cause))
        }
    }

    private fun handleBroadcasterStart() {
        viewModelScope.launchCatchError(block = {
            updateChannelStatus(PlayChannelStatusType.Live)
            getChannelById(channelId)
            _uiEvent.emit(PlayBroadcastEvent.BroadcastStarted)
            startWebSocket()
            getPinnedMessage()
            getInteractiveConfig()
        }) {
            logger.logBroadcastError(it)
            _uiEvent.emit(PlayBroadcastEvent.ShowError(it) {
                handleBroadcasterStart()
            })
        }
    }

    private fun handleBroadcasterResume(startedBefore: Boolean, shouldContinue: Boolean) {
        viewModelScope.launch {
            if (!shouldContinue) {
                _uiEvent.emit(PlayBroadcastEvent.ShowResumeLiveDialog)
            } else {
                if (startedBefore && broadcastTimer.isPastPauseDuration) {
                    _uiEvent.emit(PlayBroadcastEvent.ShowLiveEndedDialog)
                } else doResumeBroadcaster(shouldContinue)
            }
        }
    }

    fun doResumeBroadcaster(shouldContinue: Boolean) {
        viewModelScope.launchCatchError(block = {
            _uiEvent.emit(PlayBroadcastEvent.ShowLoading)
            val channelInfo = getChannelInfo()
            if (channelInfo.status == ChannelStatus.Pause
                || channelInfo.status == ChannelStatus.Live) {
                if (!shouldContinue) {
                    _uiEvent.emit(PlayBroadcastEvent.ShowResumeLiveDialog)
                } else {
                    _uiEvent.emit(PlayBroadcastEvent.BroadcastReady(channelInfo.ingestUrl))
                }
            } else _uiEvent.emit(PlayBroadcastEvent.ShowLiveEndedDialog)
        }) {
            logger.logBroadcastError(it)
            _uiEvent.emit(PlayBroadcastEvent.ShowError(it) {
                doResumeBroadcaster(shouldContinue)
            })
        }
    }

    private fun handleBroadcasterRecovered() {
        viewModelScope.launchCatchError(block = {
            val channelInfo = getChannelInfo()
            if (channelInfo.status.isPause || channelInfo.status.isLive) {
                    if (channelInfo.status.isPause) updateChannelStatus(PlayChannelStatusType.Live)
                _uiEvent.emit(PlayBroadcastEvent.BroadcastRecovered)
                updateCurrentInteractiveStatus()
            } else _uiEvent.emit(PlayBroadcastEvent.ShowLiveEndedDialog)
        }) {
            logger.logBroadcastError(it)
            _uiEvent.emit(PlayBroadcastEvent.ShowError(it) {
                handleBroadcasterRecovered()
            })
        }
    }

    private suspend fun getChannelInfo(): ChannelInfoUiModel {
        val err = getChannelById(channelId)
        if (err != null) throw err
        val channelInfo = (_observableChannelInfo.value as? NetworkResult.Success)?.data
        return channelInfo ?: throw IllegalStateException("this error not supposed to happen")
    }

    private fun handleBroadcasterPause() {
        viewModelScope.launchCatchError(block = {
            broadcastTimer.pause()
            updateChannelStatus(PlayChannelStatusType.Pause)
        }) {
            logger.logBroadcastError(it)
        }
    }

    private fun handleBroadcasterStop() {
        viewModelScope.launchCatchError(block = {
            closeWebSocket()
            updateChannelStatus(PlayChannelStatusType.Stop)
        }) {
            logger.logBroadcastError(it)
        }

        mIsBroadcastStopped = true
    }

    private fun handleGetAccountList(selectedType: String = TYPE_UNKNOWN) {
        viewModelScope.launchCatchError(block = {
            _accountStateInfo.value = AccountStateInfo()
            _observableConfigInfo.value = NetworkResult.Loading

            val accountList = repo.getAccountList()

            _accountListState.value = accountList

            if (accountList.isNotEmpty()) {
                updateSelectedAccount(
                    getSelectedAccount(
                        selectedType = selectedType,
                        cacheSelectedType = sharedPref.getLastSelectedAccount(),
                        accountList = accountList
                    )
                )
                getConfiguration(_selectedAccount.value)
            } else throw Throwable()
        }, onError = {
            _observableConfigInfo.value = NetworkResult.Fail(it) { this.handleGetAccountList(selectedType) }
        })
    }

    private fun getSelectedAccount(
        selectedType: String,
        cacheSelectedType: String,
        accountList: List<ContentAccountUiModel>,
    ): ContentAccountUiModel {
        return accountList.firstOrNull {
            it.type == when {
                selectedType != TYPE_UNKNOWN -> selectedType
                GlobalConfig.isSellerApp() -> TYPE_SHOP
                cacheSelectedType.isNotEmpty() -> cacheSelectedType
                else -> null
            }
        } ?: getDefaultSelectedAccount(accountList)
    }

    private fun getDefaultSelectedAccount(accountList: List<ContentAccountUiModel>): ContentAccountUiModel {
        val sellerAccount = accountList.firstOrNull { it.type == TYPE_SHOP }
        val nonSellerAccount = accountList.firstOrNull { it.type == TYPE_USER }
        return if (sellerAccount != null) {
            if (sellerAccount.hasAcceptTnc) sellerAccount
            else if (nonSellerAccount != null) {
                if (nonSellerAccount.hasUsername && nonSellerAccount.hasAcceptTnc) nonSellerAccount
                else sellerAccount
            } else sellerAccount
        } else nonSellerAccount ?: ContentAccountUiModel.Empty
    }

    private fun handleSwitchAccount(needLoading: Boolean = true) {
        if (needLoading) _observableConfigInfo.value = NetworkResult.Loading

        val currentSelected = switchAccount(
            when (_selectedAccount.value.type) {
                TYPE_SHOP -> TYPE_USER
                else -> TYPE_SHOP
            }
        )
        getConfiguration(currentSelected)
    }

    private fun switchAccount(selectedType: String): ContentAccountUiModel {
        return _accountListState.value.first { it.type == selectedType }
    }

    private fun isAccountEligible(
        configUiModel: ConfigurationUiModel,
        selectedAccount: ContentAccountUiModel,
    ): Boolean {
        return when {
            configUiModel.channelStatus == ChannelStatus.Live -> {
                if (isFirstOpen && isAllowChangeAccount) return false
                warningInfoType = WarningType.LIVE
                _accountStateInfo.update { AccountStateInfo() }
                _accountStateInfo.update {
                    AccountStateInfo(
                        type = AccountStateInfoType.Live,
                        selectedAccount = selectedAccount,
                    )
                }
                false
            }
            selectedAccount.isUser && !selectedAccount.hasUsername -> {
                if (isFirstOpen && isAllowChangeAccount) return false
                _accountStateInfo.update { AccountStateInfo() }
                _accountStateInfo.update {
                    AccountStateInfo(
                        type = AccountStateInfoType.NoUsername,
                        selectedAccount = selectedAccount,
                    )
                }
                false
            }
            !selectedAccount.hasAcceptTnc -> {
                if (isFirstOpen && isAllowChangeAccount) return false
                _accountStateInfo.update { AccountStateInfo() }
                _accountStateInfo.update {
                    AccountStateInfo(
                        type = AccountStateInfoType.NotAcceptTNC,
                        selectedAccount = selectedAccount,
                    )
                }
                if (selectedAccount.isShop) tncList = configUiModel.tnc
                false
            }
            else -> true
        }
    }

    private fun updateSelectedAccount(selectedAccount: ContentAccountUiModel) {
        _selectedAccount.update { selectedAccount }
        sharedPref.setLastSelectedAccount(selectedAccount.type)
        hydraConfigStore.setAuthor(selectedAccount)
    }

    /**
     * UI
     */
    private suspend fun onRetrievedNewChat(newChat: PlayChatUiModel) =
        withContext(dispatcher.main) {
            val currentChatList = _observableChatList.value ?: mutableListOf()
            currentChatList.add(newChat)
            _observableChatList.value = currentChatList
        }

    private fun queueNewMetrics(metricList: List<PlayMetricUiModel>) {
        viewModelScope.launch(dispatcher.main) {
            _observableNewMetrics.value = Event(metricList)
        }
    }

    private fun retrieveNewChat(newChat: PlayChatUiModel) {
        viewModelScope.launch(dispatcher.io) {
            onRetrievedNewChat(newChat)
        }
    }

    private fun generateShareLink(shareUiModel: ShareUiModel) {
        viewModelScope.launch {
            PlayShareWrapper.generateShareLink(shareUiModel) {
                _observableShareInfo.value = it
            }
        }
    }

    fun getBeforeLiveCountDownDuration(): Int {
        val configInfo = _observableConfigInfo.value
        return if (configInfo is NetworkResult.Success) configInfo.data.countDown.toInt()
        else DEFAULT_BEFORE_LIVE_COUNT_DOWN
    }

    fun getAuthorImage(): String = hydraConfigStore.getAuthor().iconUrl

    private fun handleClickPin(product: ProductUiModel){
        viewModelScope.launchCatchError(block = {
            product.updatePinProduct(isLoading = true)
            val result = repo.setPinProduct(channelId, product)
            if(result)
                product.updatePinProduct(isLoading = false, needToUpdate = true)
        }){
            product.updatePinProduct(isLoading = false)
            _uiEvent.emit(PlayBroadcastEvent.FailPinUnPinProduct(it, product.pinStatus.isPinned))
        }
    }

    private fun ProductUiModel.updatePinProduct(isLoading: Boolean = false, needToUpdate: Boolean = false) {
        _productSectionList.update { sectionList ->
            sectionList.map { sectionUiModel ->
                sectionUiModel.copy(campaignStatus = sectionUiModel.campaignStatus, products =
                sectionUiModel.products.map { prod ->
                    if (prod.id == this.id)
                        prod.copy(
                            pinStatus = this.pinStatus.copy(
                                isLoading = isLoading,
                                isPinned = if (needToUpdate) this.pinStatus.isPinned.switch() else this.pinStatus.isPinned
                            )
                        )
                    else
                        prod
                })
            }
        }
    }

    fun startTimer() {
        broadcastTimer.start()
    }

    fun stopTimer() {
        broadcastTimer.stop()
    }

    /**
     * Logger
     */
    private fun logChannelStatus(status: ChannelStatus) {
        logger.logChannelStatus(status)
    }

    private fun logPusherState(state: PlayBroadcasterState) {
        logger.logPusherState(state)
    }

    private fun logSocket(type: PlaySocketType) {
        logger.logSocketType(type)
    }

    fun sendBroadcasterLog(metric: BroadcasterMetric) {
        val mappedMetric = playBroadcastMapper.mapBroadcasterMetric(
            metric = metric,
            authorId = userSession.userId,
            channelId = channelId
        )
        logger.sendBroadcasterLog(mappedMetric)
    }

    companion object {

        private const val UI_STATE_STOP_TIMEOUT = 5000L

        private const val KEY_TITLE = "title"
        private const val KEY_CONFIG = "config_ui_model"
        private const val KEY_IS_LIVE_STREAM_ENDED = "key_is_live_stream_ended"
        private const val KEY_AUTHOR = "key_author"

        private const val INTERACTIVE_GQL_CREATE_DELAY = 3000L
        private const val INTERACTIVE_GQL_LEADERBOARD_DELAY = 3000L

        private const val DEFAULT_BEFORE_LIVE_COUNT_DOWN = 5
        private const val DEFAULT_QUIZ_DURATION_PICKER_IN_MINUTE = 5L
        private const val DEFAULT_GAME_RESULT_COACHMARK_AUTO_DISMISS = 5000L
        private const val FLAG_END_CURSOR = "-1"
        private const val WEB_SOCKET_SOURCE_PLAY_BROADCASTER = "Broadcaster"
    }
}
