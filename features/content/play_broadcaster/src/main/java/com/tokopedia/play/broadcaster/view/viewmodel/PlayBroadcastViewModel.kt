package com.tokopedia.play.broadcaster.view.viewmodel

import android.content.Context
import android.os.Handler
import androidx.lifecycle.*
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.broadcaster.mediator.LivePusherStatistic
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.InteractiveDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.socket.PlayBroadcastWebSocketMapper
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.model.socket.PinnedMessageSocketResponse
import com.tokopedia.play.broadcaster.domain.model.socket.SectionedProductTagSocketResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.pusher.*
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.game.GameType
import com.tokopedia.play.broadcaster.ui.model.game.quiz.*
import com.tokopedia.play.broadcaster.ui.model.interactive.*
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.ui.model.pusher.PlayLiveLogState
import com.tokopedia.play.broadcaster.ui.model.result.NetworkState
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.ui.state.*
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.removeUnusedField
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.setupAutoAddField
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.setupEditable
import com.tokopedia.play.broadcaster.util.game.quiz.QuizOptionListExt.updateQuizOptionFlow
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.util.state.PlayLiveChannelStateListener
import com.tokopedia.play.broadcaster.util.state.PlayLiveTimerStateListener
import com.tokopedia.play.broadcaster.util.state.PlayLiveViewStateListener
import com.tokopedia.play.broadcaster.view.state.*
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.domain.model.interactive.GiveawayResponse
import com.tokopedia.play_common.domain.model.interactive.QuizResponse
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.model.ui.QuizChoicesUiModel
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.play_common.util.extension.combine
import com.tokopedia.play_common.util.extension.setValue
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketClosedReason
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastViewModel @AssistedInject constructor(
    @Assisted private val handle: SavedStateHandle,
    private val livePusherMediator: PusherMediator,
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
    private val logger: PlayLogger
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
        get() = livePusherMediator.remainingDurationInMillis

    val observableConfigInfo: LiveData<NetworkResult<ConfigurationUiModel>>
        get() = _observableConfigInfo
    val observableTotalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val observableTotalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableLiveViewState: LiveData<PlayLiveViewState>
        get() = _observableLiveViewState
    val observableLiveTimerState: LiveData<PlayLiveTimerState>
        get() = _observableLiveTimerState
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
    val observableLeaderboardInfo: LiveData<NetworkResult<PlayLeaderboardInfoUiModel>>
        get() = _observableLeaderboardInfo
    val shareContents: String
        get() = _observableShareInfo.value.orEmpty()
    val interactiveId: String
        get() = getCurrentSetupDataStore().getInteractiveId()
    val activeInteractiveTitle: String
        get() = getCurrentSetupDataStore().getActiveInteractiveTitle()
    val interactiveDurations: List<Long>
        get() = findSuitableInteractiveDurations()
    val observableLivePusherStatistic: LiveData<LivePusherStatistic>
        get() = _observableLivePusherStats
    val observableLivePusherInfo: LiveData<PlayLiveLogState>
        get() = _observableLivePusherInfo

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
    private val _observableLiveViewState = MutableLiveData<PlayLiveViewState>()
    private val _observableLiveTimerState = MutableLiveData<PlayLiveTimerState>()
    private val _observableEvent = MutableLiveData<EventUiModel>()
    private val _observableLeaderboardInfo =
        MutableLiveData<NetworkResult<PlayLeaderboardInfoUiModel>>()
    private val _observableLivePusherStats = MutableLiveData<LivePusherStatistic>()
    private val _observableLivePusherInfo = MutableLiveData<PlayLiveLogState>()

    private val _configInfo = MutableStateFlow<ConfigurationUiModel?>(null)
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

    private val _interactive = MutableStateFlow<InteractiveUiModel>(InteractiveUiModel.Unknown)
    private val _interactiveConfig = MutableStateFlow(InteractiveConfigUiModel.empty())
    private val _interactiveSetup = MutableStateFlow(InteractiveSetupUiModel.Empty)

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
        quizBottomSheetUiState ->
        PlayBroadcastUiState(
            channel = channelState,
            pinnedMessage = pinnedMessage,
            selectedProduct = productMap,
            schedule = schedule,
            isExiting = isExiting,
            quizForm = quizForm,
            interactive = interactive,
            interactiveConfig = interactiveConfig,
            interactiveSetup = interactiveSetup,
            quizDetail = quizDetail,
            onBoarding = onBoarding,
            quizBottomSheetUiState = quizBottomSheetUiState,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(UI_STATE_STOP_TIMEOUT),
        PlayBroadcastUiState.Empty,
    )

    private val _uiEvent = MutableSharedFlow<PlayBroadcastEvent>(extraBufferCapacity = 100)
    val uiEvent: Flow<PlayBroadcastEvent>
        get() = _uiEvent

    private val ingestUrl: String
        get() = hydraConfigStore.getIngestUrl()

    private val liveViewStateListener = object : PlayLiveViewStateListener {
        override fun onLivePusherViewStateChanged(viewState: PlayLiveViewState) {
            when {
                viewState.isStarted -> startWebSocket()
                // TODO("find the best way to handle edge cases engagement tools")
                viewState.isRecovered -> updateCurrentInteractiveStatus()
            }
            sendLivePusherState(viewState)
        }

        override fun onReachMaximumPausePeriod() {
            stopLiveStream(shouldNavigate = true)
        }
    }

    private val liveChannelStateListener = object : PlayLiveChannelStateListener {
        override fun onChannelStateChanged(channelStatusType: PlayChannelStatusType) {
            viewModelScope.launchCatchError(block = {
                updateChannelStatus(channelStatusType)
            }) {}
        }
    }

    private val livePusherStatisticListener = object : PlayLivePusherMediatorListener {
        override fun onLivePusherStatsUpdated(statistic: LivePusherStatistic) {
            sendLivePusherStats(statistic)
        }
    }

    private val liveTimerStateListener = object : PlayLiveTimerStateListener {
        override fun onLiveTimerStateChanged(timerState: PlayLiveTimerState) {
            if (timerState == PlayLiveTimerState.Finish) {
                val event = _observableEvent.value
                if (event == null || (!event.freeze && !event.banned)) {
                    _observableLiveTimerState.value = timerState
                    stopLiveStream()
                }
            } else {
                _observableLiveTimerState.value = timerState
            }
        }
    }

    private val livePusherStateChangedListener = object : PlayLivePusherMediatorListener {
        override fun onLivePusherStateChanged(state: PlayLivePusherMediatorState) {
            logger.logPusherState(state)
            _observableLivePusherInfo.value = PlayLiveLogState.Changed(state)
        }
    }

    private var isLiveStarted: Boolean = false

    private var socketJob: Job? = null

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
        livePusherMediator.addListener(liveViewStateListener)
        livePusherMediator.addListener(liveChannelStateListener)
        livePusherMediator.addListener(liveTimerStateListener)
        if (GlobalConfig.DEBUG) livePusherMediator.addListener(livePusherStatisticListener)
        livePusherMediator.addListener(livePusherStateChangedListener)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        livePusherMediator.clearListener()
        livePusherMediator.destroy()
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

            /** Game */
            is PlayBroadcastAction.ClickGameOption -> handleClickGameOption(event.gameType)

            /** Quiz */
            PlayBroadcastAction.ClickBackOnQuiz -> handleClickBackOnQuiz()
            PlayBroadcastAction.ClickNextOnQuiz -> handleClickNextOnQuiz()
            is PlayBroadcastAction.InputQuizTitle -> handleInputQuizTitle(event.title)
            is PlayBroadcastAction.InputQuizOption -> handleInputQuizOption(event.order, event.text)
            is PlayBroadcastAction.SelectQuizOption -> handleSelectQuizOption(event.order)
            is PlayBroadcastAction.InputQuizGift -> handleInputQuizGift(event.text)
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
        }
    }

    fun getCurrentSetupDataStore(): PlayBroadcastSetupDataStore {
        return mDataStore.getSetupDataStore()
    }

    fun getConfiguration() {
        viewModelScope.launchCatchError(block = {
            _observableConfigInfo.value = NetworkResult.Loading

            val configUiModel = repo.getChannelConfiguration()
            setChannelId(configUiModel.channelId)

            _configInfo.value = configUiModel

            // create channel when there are no channel exist
            if (configUiModel.channelType == ChannelType.Unknown) createChannel()

            // get channel when channel status is paused
            if (configUiModel.channelType == ChannelType.Pause
                // also when complete draft is true
                || configUiModel.channelType == ChannelType.CompleteDraft
                || configUiModel.channelType == ChannelType.Draft
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

            _observableConfigInfo.value = NetworkResult.Success(configUiModel)

            setProductConfig(configUiModel.productTagConfig)
            setCoverConfig(configUiModel.coverConfig)
            setDurationConfig(configUiModel.durationConfig)
            setScheduleConfig(configUiModel.scheduleConfig)

            // configure live streaming duration
            livePusherMediator.setLiveStreamingDuration(
                if (configUiModel.channelType == ChannelType.Pause) configUiModel.durationConfig.duration - configUiModel.remainingTime
                else 0,
                configUiModel.durationConfig.duration
            )
            livePusherMediator.setLiveStreamingPauseDuration(configUiModel.durationConfig.pauseDuration)

        }) {
            _observableConfigInfo.value = NetworkResult.Fail(it) { this.getConfiguration() }
        }
    }

    private suspend fun getChannelDetail() = getChannelById(channelId)

    private suspend fun createChannel() {
        val channelId = repo.createChannel()
        setChannelId(channelId)
    }

    private suspend fun getChannelById(channelId: String): Throwable? {
        _observableChannelInfo.value = NetworkResult.Loading
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

            logger.logChannelStatus(channelInfo.status)

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
            setBroadcastSchedule(playBroadcastMapper.mapChannelSchedule(channel.basic.timestamp))

            generateShareLink(playBroadcastMapper.mapShareInfo(channel))

            null
        } catch (err: Throwable) {
            _observableChannelInfo.value = NetworkResult.Fail(err)
            err
        }
    }

    private suspend fun updateChannelStatus(status: PlayChannelStatusType) {
        repo.updateChannelStatus(channelId, status)
    }

    @Throws(IllegalAccessException::class)
    fun createStreamer(context: Context, handler: Handler) {
        livePusherMediator.init(context, handler)
    }

    fun switchCamera() {
        livePusherMediator.switchCamera()
    }

    fun startPreview(surfaceView: SurfaceAspectRatioView) {
        livePusherMediator.onCameraChanged(surfaceView)
    }

    fun stopPreview() {
        livePusherMediator.onCameraDestroyed()
    }

    fun setFirstTimeLiveStreaming() {
        sharedPref.setNotFirstStreaming()
    }

    fun startLiveStream(withTimer: Boolean = true) {
        livePusherMediator.startLiveStreaming(ingestUrl, withTimer)
        getPinnedMessage()
        displayGameResultWidgetIfHasLeaderBoard()
        if (withTimer) {
            // TODO("find the best way to trigger engagement tools")
            getInteractiveConfig()
        }
        isLiveStarted = true
        _observableLivePusherInfo.value =
            playBroadcastMapper.mapLiveInfo(livePusherMediator.ingestUrl, livePusherMediator.config)
    }

    fun reconnectLiveStream() {
        sendLivePusherState(PlayLiveViewState.Connecting)
        fun reconnectJob() {
            viewModelScope.launch {
                val err = getChannelDetail()
                if (err == null && _observableChannelInfo.value is NetworkResult.Success) {
                    val channelInfo = (_observableChannelInfo.value as NetworkResult.Success).data
                    when (channelInfo.status) {
                        PlayChannelStatusType.Pause,
                        PlayChannelStatusType.Live -> livePusherMediator.resume()
                        else -> stopLiveStream(shouldNavigate = true)
                    }
                } else {
                    sendLivePusherState(
                        PlayLiveViewState.Error(
                            PlayLivePusherException("network: Failed to get channel details")
                        )
                    )
                    reconnectJob()
                }
            }
        }
        reconnectJob()
    }

    fun startLiveTimer() {
        viewModelScope.launch {
            delay(START_LIVE_TIMER_DELAY)
            livePusherMediator.startLiveTimer()
        }
        // TODO("find the best way to trigger engagement tools")
        getInteractiveConfig()
    }

    fun continueLiveStream() {
        if (isLiveStarted) reconnectLiveStream()
        else startLiveStream()
    }

    fun stopLiveStream(shouldNavigate: Boolean = false) {
        viewModelScope.launchCatchError(block = {
            _isExiting.value = true
            updateChannelStatus(PlayChannelStatusType.Stop)
            closeWebSocket()
            livePusherMediator.stopLiveStreaming()
            sendLivePusherState(PlayLiveViewState.Stopped(shouldNavigate))
            _isExiting.value = false
        }) {
            _isExiting.value = false
            _uiEvent.emit(PlayBroadcastEvent.ShowError(it))
        }
    }

    fun setChannelId(channelId: String) {
        hydraConfigStore.setChannelId(channelId)
    }

    fun sendLogs() {
        try {
            logger.sendAll(channelId)
        } catch (ignored: IllegalStateException) {
        }
    }

    private fun sendLivePusherState(state: PlayLiveViewState) {
        viewModelScope.launch(dispatcher.main) {
            _observableLiveViewState.value = state
        }
    }

    private fun setInteractiveTitle(title: String) {
        getCurrentSetupDataStore().setSetupInteractiveTitle(title)
    }

    private fun setActiveInteractiveTitle(title: String) {
        getCurrentSetupDataStore().setActiveInteractiveTitle(title)
    }

    private fun setSelectedInteractiveDuration(durationInMs: Long) {
        getCurrentSetupDataStore().setSelectedInteractiveDuration(durationInMs)
    }

    private fun isCreateSessionAllowed(durationInMs: Long): Boolean {
        val remainingLiveDuration = livePusherMediator.remainingDurationInMillis
        val delayGqlDuration = INTERACTIVE_GQL_CREATE_DELAY
        return remainingLiveDuration > durationInMs + delayGqlDuration
    }

    fun getLeaderboardData(channelId: String) {
        viewModelScope.launch { getLeaderboardInfo(channelId) }
    }

    private fun getInteractiveConfig() {
        viewModelScope.launchCatchError(block = {
            val gameConfig = repo.getInteractiveConfig()

            _interactiveConfig.value = mergeInteractiveConfigWithPreference(gameConfig)

            /** TODO: should save config on flow instead */
            setInteractiveDurations(gameConfig.giveawayConfig.availableStartTimeInMs)

            if (!gameConfig.isNoGameActive()) {
                initQuizFormData()
                handleActiveInteractive()
            }
        }) { }
    }

    private fun mergeInteractiveConfigWithPreference(
        gameConfig: InteractiveConfigUiModel
    ): InteractiveConfigUiModel {
        return gameConfig.copy(
            quizConfig = gameConfig.quizConfig.copy(
                showPrizeCoachMark = sharedPref.isFirstQuizPrice(),
            )
        )
    }

    private fun updateCurrentInteractiveStatus() {
        viewModelScope.launch {
            val interactiveConfig = _interactiveConfig.value
            if (!interactiveConfig.isNoGameActive()) handleActiveInteractive()
        }
    }

    private suspend fun handleActiveInteractive() {
        try {
            val currentInteractive = repo.getCurrentInteractive(channelId)
            handleActiveInteractiveFromNetwork(currentInteractive)
        } catch (e: Throwable) {
            _interactive.value = InteractiveUiModel.Unknown
        }
    }

    private fun handleActiveInteractiveFromNetwork(interactive: InteractiveUiModel) {
        setInteractiveId(interactive.id)
        setActiveInteractiveTitle(interactive.title)
        when (interactive) {
            is InteractiveUiModel.Giveaway -> setupGiveaway(interactive)
            is InteractiveUiModel.Quiz -> setupQuiz(interactive)
            else -> {}
        }
    }

    private fun setupGiveaway(giveaway: InteractiveUiModel.Giveaway) {
        _interactive.value = giveaway
        when (giveaway.status) {
            InteractiveUiModel.Giveaway.Status.Finished -> displayGameResultWidgetIfHasLeaderBoard()
            InteractiveUiModel.Giveaway.Status.Unknown -> stopInteractive()
        }
    }

    private fun setupQuiz(quiz: InteractiveUiModel.Quiz) {
        _interactive.value = quiz
        when (quiz.status) {
            InteractiveUiModel.Quiz.Status.Finished -> displayGameResultWidgetIfHasLeaderBoard()
            InteractiveUiModel.Quiz.Status.Unknown -> stopInteractive()
        }
    }

    private suspend fun getLeaderboardInfo(channelId: String): Throwable? {
        _observableLeaderboardInfo.value = NetworkResult.Loading
        return try {
            val leaderboard = repo.getInteractiveLeaderboard(channelId) {
                livePusherMediator.getLivePusherState().isStopped
            }
            _observableLeaderboardInfo.value = NetworkResult.Success(leaderboard)
            null
        } catch (err: Throwable) {
            _observableLeaderboardInfo.value = NetworkResult.Fail(err)
            err
        }
    }

    private fun findSuitableInteractiveDurations(): List<Long> {
        updateRemainingLiveDuration()
        return getCurrentSetupDataStore().getInteractiveDurations()
    }

    private fun sendLivePusherStats(stats: LivePusherStatistic) {
        viewModelScope.launch(dispatcher.main) {
            _observableLivePusherStats.value = stats
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

    private fun connectWebSocket(
        channelId: String,
        socketCredential: GetSocketCredentialResponse.SocketCredential
    ) {
        playBroadcastWebSocket.connect(
            channelId,
            socketCredential.gcToken,
            WEB_SOCKET_SOURCE_PLAY_BROADCASTER
        )
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
                // TODO: need to change this validation, instead of remaining changes this to currDuration == maxDuration
                if (result.remaining <= 0) logger.logSocketType(result)
                restartLiveDuration(result)
            }
            is SectionedProductTagSocketResponse -> {
                setSelectedProduct(productMapper.mapSectionedProduct(result))
            }
            is Chat -> retrieveNewChat(playBroadcastMapper.mapIncomingChat(result))
            is Freeze -> {
                if (_observableLiveTimerState.value !is PlayLiveTimerState.Finish) {
                    val eventUiModel =
                        playBroadcastMapper.mapFreezeEvent(result, _observableEvent.value)
                    if (eventUiModel.freeze) {
                        logger.logSocketType(result)
                        stopLiveStream()
                        _observableEvent.value = eventUiModel
                    }
                }
            }
            is Banned -> {
                if (_observableLiveTimerState.value !is PlayLiveTimerState.Finish) {
                    val eventUiModel =
                        playBroadcastMapper.mapBannedEvent(result, _observableEvent.value)
                    if (eventUiModel.banned) {
                        logger.logSocketType(result)
                        stopLiveStream()
                        _observableEvent.value = eventUiModel
                    }
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
            _configInfo.value?.durationConfig?.duration?.let {
                val durationInMillis = TimeUnit.SECONDS.toMillis(duration.duration)
                livePusherMediator.restartLiveTimer(durationInMillis, it)
            }
        }) { }
    }

    private fun setInteractiveDurations(durations: List<Long>) {
        getCurrentSetupDataStore().setInteractiveDurations(durations)
    }

    private fun updateRemainingLiveDuration() {
        getCurrentSetupDataStore().setRemainingLiveDuration(livePusherMediator.remainingDurationInMillis)
    }

    private fun resetSetupInteractive() {
        setInteractiveTitle(InteractiveDataStoreImpl.DEFAULT_INTERACTIVE_TITLE)
        setSelectedInteractiveDuration(InteractiveDataStoreImpl.DEFAULT_INTERACTIVE_DURATION)
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
                listOf(
                    playBroadcastMapper.mapQuizDetailToLeaderBoard(quizDetailUiModel)
                )
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
            val leaderboardSlots = repo.getSellerLeaderboardWithSlot(channelId, allowChat)
            _quizDetailState.value = QuizDetailStateUiModel.Success(leaderboardSlots)
        }) {
            _quizDetailState.value =
                QuizDetailStateUiModel.Error(allowChat = allowChat, isQuizDetail = false)
        }
    }

    private fun displayGameResultWidgetIfHasLeaderBoard() {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            val leaderboardSlots = repo.getSellerLeaderboardWithSlot(channelId, false)
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
                 * TODO("Quiz to also use [_interactiveSetup], because only 1 interactive can be active at a time")
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

    private fun handleInputQuizGift(text: String) {
        needUpdateQuizForm(false) {
            _quizFormData.setValue { copy(gift = text) }
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
                prize = quizData.gift,
                runningTime = durationInSecond,
                choices = quizData.options.map { playBroadcastMapper.mapQuizOptionToChoice(it) },
            )

            handleActiveInteractive()

            /** Reset Form */
            sharedPref.setNotFirstSelectQuizOption()
            sharedPref.setNotFirstQuizPrice()
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
     * When pre-start finished, interactive should be played (e.g. TapTap)
     */
    private fun handleGiveawayUpcomingEnded() {
        viewModelScope.launchCatchError(dispatcher.computation, block = {
            _interactive.update {
                val currentGiveaway =
                    it as? InteractiveUiModel.Giveaway ?: error("Interactive is not giveaway")
                val upcomingStatus =
                    currentGiveaway.status as? InteractiveUiModel.Giveaway.Status.Upcoming
                        ?: error("Giveaway status is not upcoming")

                val interactive = it.copy(
                    status = InteractiveUiModel.Giveaway.Status.Ongoing(
                        endTime = upcomingStatus.endTime
                    )
                )
                interactive
            }

        }) {
            _interactive.value = InteractiveUiModel.Unknown
        }
    }

    private fun handleGiveawayOngoingEnded() {
        viewModelScope.launchCatchError(dispatcher.computation, block = {
            val interactive = _interactive.getAndUpdate {
                if (it !is InteractiveUiModel.Giveaway) error("Interactive is not giveaway")
                val newInteractive = it.copy(
                    status = InteractiveUiModel.Giveaway.Status.Finished
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
            _interactive.value = InteractiveUiModel.Unknown
        }) {
            _interactive.value = InteractiveUiModel.Unknown
        }
    }

    private fun handleQuizEnded() {
        viewModelScope.launchCatchError(dispatcher.computation, block = {
            val interactive = _interactive.getAndUpdate {
                if (it !is InteractiveUiModel.Quiz) error("Interactive is not quiz")
                val interactive = it.copy(
                    status = InteractiveUiModel.Quiz.Status.Finished
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
            _interactive.value = InteractiveUiModel.Unknown
        }) {
            _interactive.value = InteractiveUiModel.Unknown
        }
    }

    private fun stopInteractive(){
        setActiveInteractiveTitle("")
        setInteractiveId("")
        displayGameResultWidgetIfHasLeaderBoard()
        _interactive.value = InteractiveUiModel.Unknown
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
            if (uiState.value.interactive is InteractiveUiModel.Quiz) {
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
        val options = _quizFormData.value.options.toMutableList()
        val quizConfig = _interactiveConfig.value.quizConfig
        val isStateEditable = isQuizStateEditable()

        val newOptions = if (isStateEditable) {
            options.setupAutoAddField(quizConfig)
        } else {
            options.removeUnusedField()
        }.setupEditable(isStateEditable)

        _quizFormData.setValue { copy(options = newOptions) }
    }

    private fun isQuizStateEditable(): Boolean {
        return _quizFormState.value !is QuizFormStateUiModel.SetDuration
    }

    private fun updateQuizEligibleDuration() {
        val remainingDuration = livePusherMediator.remainingDurationInMillis
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

    fun getShopIconUrl(): String = userSession.shopAvatar

    fun getShopName(): String = userSession.shopName

    companion object {

        private const val UI_STATE_STOP_TIMEOUT = 5000L

        private const val KEY_TITLE = "title"

        private const val INTERACTIVE_GQL_CREATE_DELAY = 3000L
        private const val INTERACTIVE_GQL_LEADERBOARD_DELAY = 3000L

        private const val START_LIVE_TIMER_DELAY = 1000L

        private const val DEFAULT_BEFORE_LIVE_COUNT_DOWN = 5
        private const val DEFAULT_QUIZ_DURATION_PICKER_IN_MINUTE = 5L
        private const val DEFAULT_GAME_RESULT_COACHMARK_AUTO_DISMISS = 5000L
        private const val FLAG_END_CURSOR = "-1"
        private const val WEB_SOCKET_SOURCE_PLAY_BROADCASTER = "Broadcaster"
    }
}