package com.tokopedia.play.broadcaster.view.viewmodel

import android.content.Context
import android.os.Handler
import androidx.lifecycle.*
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.InteractiveDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.data.model.SerializableHydraSetupData
import com.tokopedia.play.broadcaster.data.socket.PlayBroadcastWebSocket
import com.tokopedia.play.broadcaster.data.socket.PlayBroadcastWebSocketMapper
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.model.socket.PinnedMessageSocketResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveConfigUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.PostInteractiveCreateSessionUseCase
import com.tokopedia.play.broadcaster.pusher.*
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.interactive.*
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageEditStatus
import com.tokopedia.play.broadcaster.ui.model.pinnedmessage.PinnedMessageUiModel
import com.tokopedia.play.broadcaster.ui.model.pusher.PlayLiveLogState
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.ui.state.PinnedMessageUiState
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastUiState
import com.tokopedia.play.broadcaster.ui.state.PlayChannelUiState
import com.tokopedia.play.broadcaster.util.error.PlayLivePusherException
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.util.state.PlayLiveChannelStateListener
import com.tokopedia.play.broadcaster.util.state.PlayLiveCountDownTimerStateListener
import com.tokopedia.play.broadcaster.util.state.PlayLiveViewStateListener
import com.tokopedia.play.broadcaster.view.custom.SurfaceAspectRatioView
import com.tokopedia.play.broadcaster.view.state.PlayLiveCountDownTimerState
import com.tokopedia.play.broadcaster.view.state.PlayLiveViewState
import com.tokopedia.play.broadcaster.view.state.isRecovered
import com.tokopedia.play.broadcaster.view.state.isStarted
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.model.interactive.ChannelInteractive
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.domain.usecase.interactive.GetInteractiveLeaderboardUseCase
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.model.dto.interactive.PlayInteractiveTimeStatus
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.model.ui.PlayLeaderboardInfoUiModel
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.play_common.util.extension.setValue
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketClosedReason
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by mzennis on 24/05/20.
 */
internal class PlayBroadcastViewModel @Inject constructor(
    private val livePusherMediator: PlayLivePusherMediator,
    private val mDataStore: PlayBroadcastDataStore,
    private val hydraConfigStore: HydraConfigStore,
    private val sharedPref: HydraSharedPreferences,
    private val getChannelUseCase: GetChannelUseCase,
    private val createChannelUseCase: CreateChannelUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val getAddedChannelTagsUseCase: GetAddedChannelTagsUseCase,
    private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
    private val getInteractiveConfigUseCase: GetInteractiveConfigUseCase,
    private val getCurrentInteractiveUseCase: GetCurrentInteractiveUseCase,
    private val getInteractiveLeaderboardUseCase: GetInteractiveLeaderboardUseCase,
    private val createInteractiveSessionUseCase: PostInteractiveCreateSessionUseCase,
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val playBroadcastWebSocket: PlayBroadcastWebSocket,
    private val playBroadcastMapper: PlayBroadcastMapper,
    private val channelInteractiveMapper: PlayChannelInteractiveMapper,
    private val interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper,
    private val repo: PlayBroadcastRepository,
    private val logger: PlayLogger
) : ViewModel() {

    val isFirstStreaming: Boolean
        get() = sharedPref.isFirstStreaming()

    val channelId: String
        get() = hydraConfigStore.getChannelId()
    val channelTitle: String
        get() {
            return when (val titleModel = mDataStore.getSetupDataStore().getTitle()) {
                is PlayTitleUiModel.HasTitle -> titleModel.title
                else -> ""
            }
        }

    val observableConfigInfo: LiveData<NetworkResult<ConfigurationUiModel>>
        get() = _observableConfigInfo
    val observableChannelInfo: LiveData<NetworkResult<ChannelInfoUiModel>>
        get() = _observableChannelInfo
    val observableTotalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val observableTotalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableLiveViewState: LiveData<PlayLiveViewState>
        get() = _observableLiveViewState
    val observableLiveCountDownTimerState: LiveData<PlayLiveCountDownTimerState>
        get() = _observableLiveCountDownTimerState
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableNewMetrics: LiveData<Event<List<PlayMetricUiModel>>>
        get() = _observableNewMetrics
    val observableProductList = getCurrentSetupDataStore().getObservableSelectedProducts()
            .map { dataList -> dataList.map { ProductContentUiModel.createFromData(it) } }
            .asLiveData(viewModelScope.coroutineContext + dispatcher.computation)
    val observableCover = getCurrentSetupDataStore().getObservableSelectedCover()
    val observableTitle: LiveData<PlayTitleUiModel.HasTitle> = getCurrentSetupDataStore().getObservableTitle()
                .filterIsInstance<PlayTitleUiModel.HasTitle>()
                .asLiveData(viewModelScope.coroutineContext + dispatcher.computation)
    val observableEvent: LiveData<EventUiModel>
        get() = _observableEvent
    val observableBroadcastSchedule = getCurrentSetupDataStore().getObservableSchedule()
    val observableInteractiveConfig: LiveData<InteractiveConfigUiModel>
        get() = _observableInteractiveConfig
    val observableInteractiveState: LiveData<BroadcastInteractiveState>
        get() = _observableInteractiveState
    val observableLeaderboardInfo: LiveData<NetworkResult<PlayLeaderboardInfoUiModel>>
        get() = _observableLeaderboardInfo
    val observableCreateInteractiveSession: LiveData<NetworkResult<InteractiveSessionUiModel>>
        get() = _observableCreateInteractiveSession
    val shareContents: String
        get() = _observableShareInfo.value.orEmpty()
    val interactiveId: String
        get() = getCurrentSetupDataStore().getInteractiveId()
    val activeInteractiveTitle: String
        get() = getCurrentSetupDataStore().getActiveInteractiveTitle()
    val setupInteractiveTitle: String
        get() = getCurrentSetupDataStore().getSetupInteractiveTitle()
    val selectedInteractiveDuration: Long
        get() = getCurrentSetupDataStore().getSelectedInteractiveDuration()
    val interactiveDurations: List<Long>
        get() = findSuitableInteractiveDurations()
    val observableLivePusherStatistic: LiveData<PlayLivePusherStatistic>
        get() = _observableLivePusherStats
    val observableLivePusherInfo: LiveData<PlayLiveLogState>
        get() = _observableLivePusherInfo

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
    private val _observableLiveCountDownTimerState = MutableLiveData<PlayLiveCountDownTimerState>()
    private val _observableEvent = MutableLiveData<EventUiModel>()
    private val _observableInteractiveConfig = MutableLiveData<InteractiveConfigUiModel>()
    private val _observableInteractiveState = MutableLiveData<BroadcastInteractiveState>()
    private val _observableLeaderboardInfo = MutableLiveData<NetworkResult<PlayLeaderboardInfoUiModel>>()
    private val _observableCreateInteractiveSession = MutableLiveData<NetworkResult<InteractiveSessionUiModel>>()
    private val _observableLivePusherStats = MutableLiveData<PlayLivePusherStatistic>()
    private val _observableLivePusherInfo = MutableLiveData<PlayLiveLogState>()

    private val _configInfo = MutableStateFlow<ConfigurationUiModel?>(null)
    private val _pinnedMessage = MutableStateFlow<PinnedMessageUiModel>(
        PinnedMessageUiModel.Empty()
    )
    private val _isExiting = MutableStateFlow(false)

    private val _channelUiState = _configInfo
        .filterNotNull()
        .map {
            PlayChannelUiState(
                canStream = it.streamAllowed,
                tnc = it.tnc,
            )
        }

    private val _pinnedMessageUiState = _pinnedMessage.map {
        PinnedMessageUiState(
            message = if (it.isActive && !it.isInvalidId) it.message else "",
            editStatus = it.editStatus
        )
    }

    val uiState = combine(
        _channelUiState.distinctUntilChanged(),
        _pinnedMessageUiState.distinctUntilChanged(),
        _isExiting
    ) { channelState, pinnedMessage, isExiting ->
        PlayBroadcastUiState(
            channel = channelState,
            pinnedMessage = pinnedMessage,
            isExiting = isExiting,
        )
    }

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
        override fun onLivePusherStatsUpdated(statistic: PlayLivePusherStatistic) {
            sendLivePusherStats(statistic)
        }
    }

    private val liveCountDownTimerStateListener = object : PlayLiveCountDownTimerStateListener {
        override fun onLiveCountDownTimerStateChanged(countDownTimerState: PlayLiveCountDownTimerState) {
            if (countDownTimerState == PlayLiveCountDownTimerState.Finish) {
                val event = _observableEvent.value
                if (event == null || (!event.freeze && !event.banned)) {
                    _observableLiveCountDownTimerState.value = countDownTimerState
                    stopLiveStream()
                }
            } else {
                _observableLiveCountDownTimerState.value = countDownTimerState
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
        _observableChatList.value = mutableListOf()
        livePusherMediator.addListener(liveViewStateListener)
        livePusherMediator.addListener(liveChannelStateListener)
        livePusherMediator.addListener(liveCountDownTimerStateListener)
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
                    || configUiModel.channelType == ChannelType.CompleteDraft) {
                val err = getChannelById(configUiModel.channelId)
                if (err != null) {
                    throw err
                }
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

    fun getHydraSetupData(): SerializableHydraSetupData {
        return mDataStore.getSerializableData()
    }

    fun setHydraSetupData(setupData: SerializableHydraSetupData) {
        mDataStore.setSerializableData(setupData)
    }

    suspend fun getChannelDetail() = getChannelById(channelId)

    private suspend fun createChannel() {
        val channelId = withContext(dispatcher.io) {
            createChannelUseCase.params = CreateChannelUseCase.createParams(
                    authorId = userSession.shopId
            )
            return@withContext createChannelUseCase.executeOnBackground()
        }
        setChannelId(channelId.id)
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

            setSelectedProduct(playBroadcastMapper.mapChannelProductTags(channel.productTags))
            setSelectedCover(playBroadcastMapper.mapCover(getCurrentSetupDataStore().getSelectedCover(), channel.basic.coverUrl))
            setBroadcastSchedule(playBroadcastMapper.mapChannelSchedule(channel.basic.timestamp))

            generateShareLink(playBroadcastMapper.mapShareInfo(channel))

            null
        } catch (err: Throwable) {
            _observableChannelInfo.value = NetworkResult.Fail(err)
            err
        }
    }

    private suspend fun updateChannelStatus(status: PlayChannelStatusType) = withContext(dispatcher.io) {
            updateChannelUseCase.apply {
                setQueryParams(
                    UpdateChannelUseCase.createUpdateStatusRequest(
                        channelId = channelId,
                        authorId = userSession.shopId,
                        status = status
                    )
                )
            }.executeOnBackground()
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
        if (withTimer) {
            // TODO("find the best way to trigger engagement tools")
            getInteractiveConfig()
        }
        isLiveStarted = true
        _observableLivePusherInfo.value = playBroadcastMapper.mapLiveInfo(livePusherMediator.ingestUrl, livePusherMediator.config)
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
                            PlayLivePusherException("connection failure: Failed to get channel details")
                        )
                    )
                    reconnectJob()
                }
            }
        }
        reconnectJob()
    }

    fun startLiveCountDownTimer() {
        viewModelScope.launch {
            delay(START_COUNTDOWN_DELAY)
            livePusherMediator.startLiveCountDownTimer()
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
        } catch (ignored: IllegalStateException) { }
    }

    private fun sendLivePusherState(state: PlayLiveViewState) {
        viewModelScope.launch(dispatcher.main) {
            _observableLiveViewState.value = state
        }
    }

    fun setInteractiveTitle(title: String) {
        getCurrentSetupDataStore().setSetupInteractiveTitle(title)
    }

    private fun setActiveInteractiveTitle(title: String) {
        getCurrentSetupDataStore().setActiveInteractiveTitle(title)
    }

    fun setSelectedInteractiveDuration(durationInMs: Long) {
        getCurrentSetupDataStore().setSelectedInteractiveDuration(durationInMs)
    }

    fun onInteractiveLiveEnded() {
        sharedPref.setNotFirstInteractive()
        viewModelScope.launch { onInteractiveFinished() }
    }

    fun createInteractiveSession(title: String, durationInMs: Long) {
        _observableCreateInteractiveSession.value = NetworkResult.Loading
        if (!isCreateSessionAllowed(durationInMs)) {
            _observableCreateInteractiveSession.value = NetworkResult.Fail(Throwable("not allowed to create session"))
            return
        }

        viewModelScope.launchCatchError(block = {
            val response = createInteractiveSessionUseCase.execute(
                userSession.shopId,
                channelId,
                title,
                durationInMs
            )
            val interactiveUiModel = playBroadcastMapper.mapInteractiveSession(response, title, durationInMs)
            setInteractiveId(interactiveUiModel.id)
            setActiveInteractiveTitle(interactiveUiModel.title)
            handleActiveInteractive()
            resetSetupInteractive()
            _observableCreateInteractiveSession.value = NetworkResult.Success(interactiveUiModel)
        }) {
            _observableCreateInteractiveSession.value = NetworkResult.Fail(it)
        }
    }

    private fun isCreateSessionAllowed(durationInMs: Long): Boolean {
        val remainingLiveDuration = livePusherMediator.remainingDurationInMillis
        val delayGqlDuration = INTERACTIVE_GQL_CREATE_DELAY
        return remainingLiveDuration > durationInMs + delayGqlDuration
    }

    fun getLeaderboardData() {
        viewModelScope.launch { getLeaderboardInfo() }
    }

    private fun getInteractiveConfig() {
        viewModelScope.launchCatchError(block = {
            val interactiveResponse = getInteractiveConfigUseCase.apply {
                setRequestParams(GetInteractiveConfigUseCase.createParams(userSession.shopId))
            }.executeOnBackground()
            val interactiveConfig = playBroadcastMapper.mapInteractiveConfig(interactiveResponse)
            _observableInteractiveConfig.value = interactiveConfig

            setInteractiveDurations(interactiveConfig.availableStartTimeInMs)

            if (interactiveConfig.isActive) {
                handleActiveInteractive()
            } else {
                _observableInteractiveState.value = BroadcastInteractiveState.Forbidden
            }
        }) {}
    }

    private fun updateCurrentInteractiveStatus() {
        viewModelScope.launch {
            val interactiveConfig = _observableInteractiveConfig.value
            if (interactiveConfig?.isActive == true) handleActiveInteractive()
        }
    }

    private suspend fun handleActiveInteractive() {
        try {
            val currentInteractiveResponse = getCurrentInteractiveUseCase.apply {
                setRequestParams(GetCurrentInteractiveUseCase.createParams(channelId))
            }.executeOnBackground()

            val currentInteractive = channelInteractiveMapper.mapInteractive(currentInteractiveResponse.data.interactive)
            handleActiveInteractiveFromNetwork(currentInteractive)
        } catch (e: Throwable) {
            _observableInteractiveState.value = getNoPreviousInitInteractiveState()
        }
    }

    private suspend fun handleActiveInteractiveFromNetwork(interactive: PlayCurrentInteractiveModel) {
        setInteractiveId(interactive.id.toString())
        setActiveInteractiveTitle(interactive.title)
        when (val status = interactive.timeStatus) {
            is PlayInteractiveTimeStatus.Scheduled -> onInteractiveScheduled(
                timeToStartInMs = status.timeToStartInMs,
                durationInMs = status.interactiveDurationInMs,
                title = interactive.title
            )
            is PlayInteractiveTimeStatus.Live -> onInteractiveLiveStarted(status.remainingTimeInMs)
            is PlayInteractiveTimeStatus.Finished -> onInteractiveFinished()
            else -> {
                _observableInteractiveState.value = getNoPreviousInitInteractiveState()
            }
        }
    }

    private fun onInteractiveScheduled(timeToStartInMs: Long, durationInMs: Long, title: String) {
        _observableInteractiveState.value = BroadcastInteractiveState.Allowed.Schedule(timeToStartInMs = timeToStartInMs, durationInMs = durationInMs, title = title)
    }

    private fun onInteractiveLiveStarted(durationInMs: Long) {
        _observableInteractiveState.value = BroadcastInteractiveState.Allowed.Live(remainingTimeInMs = durationInMs)
    }

    private suspend fun onInteractiveFinished() {
        _observableInteractiveState.value = BroadcastInteractiveState.Allowed.Init(state = BroadcastInteractiveInitState.Loading)
        delay(INTERACTIVE_GQL_LEADERBOARD_DELAY)
        val err = getLeaderboardInfo()
        if (err == null && _observableLeaderboardInfo.value is NetworkResult.Success) {
            val leaderboard = (_observableLeaderboardInfo.value as NetworkResult.Success).data
            val coachMark = if (leaderboard.leaderboardWinners.firstOrNull()?.winners.isNullOrEmpty()) BroadcastInteractiveCoachMark.NoCoachMark else BroadcastInteractiveCoachMark.HasCoachMark(
                    leaderboard.config.loserMessage,
                    leaderboard.config.sellerMessage
            )
            _observableInteractiveState.value = BroadcastInteractiveState.Allowed.Init(state = BroadcastInteractiveInitState.HasPrevious(coachMark))
        } else {
            _observableInteractiveState.value = getNoPreviousInitInteractiveState()
        }
    }

    private suspend fun getLeaderboardInfo(): Throwable? {
        _observableLeaderboardInfo.value = NetworkResult.Loading
        return try {
            val leaderboardResponse = getInteractiveLeaderboardUseCase.execute(channelId)
            val leaderboard = interactiveLeaderboardMapper.mapLeaderboard(leaderboardResponse) { livePusherMediator.state.isStopped }
            _observableLeaderboardInfo.value = NetworkResult.Success(leaderboard)
            null
        } catch (err: Throwable) {
            _observableLeaderboardInfo.value = NetworkResult.Fail(err)
            err
        }
    }

    private fun getNoPreviousInitInteractiveState(): BroadcastInteractiveState {
        return BroadcastInteractiveState.Allowed.Init(state = BroadcastInteractiveInitState.NoPrevious(sharedPref.isFirstInteractive()))
    }

    private fun findSuitableInteractiveDurations(): List<Long> {
        updateRemainingLiveDuration()
        return getCurrentSetupDataStore().getInteractiveDurations()
    }

    private fun sendLivePusherStats(stats: PlayLivePusherStatistic) {
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

    private fun connectWebSocket(channelId: String, socketCredential: GetSocketCredentialResponse.SocketCredential) {
        playBroadcastWebSocket.connectSocket(channelId, socketCredential.gcToken)
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
            is WebSocketAction.Closed -> if (response.reason is WebSocketClosedReason.Error) connectWebSocket(channelId, socketCredential)
        }
    }

    private suspend fun handleWebSocketMessage(message: WebSocketResponse) {
        val result = withContext(dispatcher.computation) {
            val socketMapper = PlayBroadcastWebSocketMapper(message, gson)
            socketMapper.map()
        }
        when(result) {
            is NewMetricList -> queueNewMetrics(playBroadcastMapper.mapNewMetricList(result))
            is TotalView -> _observableTotalView.value = playBroadcastMapper.mapTotalView(result)
            is TotalLike -> _observableTotalLike.value = playBroadcastMapper.mapTotalLike(result)
            is LiveDuration -> {
                // TODO: need to change this validation, instead of remaining changes this to currDuration == maxDuration
                if (result.remaining <= 0) logger.logSocketType(result)
                restartLiveDuration(result)
            }
            is ProductTagging -> setSelectedProduct(playBroadcastMapper.mapProductTag(result))
            is Chat -> retrieveNewChat(playBroadcastMapper.mapIncomingChat(result))
            is Freeze -> {
                if (_observableLiveCountDownTimerState.value !is PlayLiveCountDownTimerState.Finish) {
                    val eventUiModel = playBroadcastMapper.mapFreezeEvent(result, _observableEvent.value)
                    if (eventUiModel.freeze) {
                        logger.logSocketType(result)
                        stopLiveStream()
                        _observableEvent.value = eventUiModel
                    }
                }
            }
            is Banned -> {
                if (_observableLiveCountDownTimerState.value !is PlayLiveCountDownTimerState.Finish) {
                    val eventUiModel = playBroadcastMapper.mapBannedEvent(result, _observableEvent.value)
                    if (eventUiModel.banned) {
                        logger.logSocketType(result)
                        stopLiveStream()
                        _observableEvent.value = eventUiModel
                    }
                }
            }
            is ChannelInteractive -> {
                val currentInteractive = channelInteractiveMapper.mapInteractive(result)
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

    private fun setSelectedProduct(products: List<ProductData>) {
        getCurrentSetupDataStore().setSelectedProducts(products)
    }

    private fun setSelectedCover(cover: PlayCoverUiModel) {
        getCurrentSetupDataStore().setFullCover(cover)
    }

    private fun setChannelTitle(title: String) {
        getCurrentSetupDataStore().setTitle(title)
    }

    private fun setBroadcastSchedule(schedule: BroadcastScheduleUiModel) {
        getCurrentSetupDataStore().setBroadcastSchedule(schedule)
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
    }

    private fun restartLiveDuration(duration: LiveDuration) {
        viewModelScope.launchCatchError(block = {
            _configInfo.value?.durationConfig?.duration?.let {
                val durationInMillis = TimeUnit.SECONDS.toMillis(duration.duration)
                livePusherMediator.restartLiveCountDownTimer(durationInMillis, it)
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

    /**
     * UI
     */
    private suspend fun onRetrievedNewChat(newChat: PlayChatUiModel) = withContext(dispatcher.main) {
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
        return if(configInfo is NetworkResult.Success) configInfo.data.countDown.toInt()
                else DEFAULT_BEFORE_LIVE_COUNT_DOWN
    }

    fun getShopIconUrl(): String = userSession.shopAvatar

    companion object {

        private const val INTERACTIVE_GQL_CREATE_DELAY = 3000L
        private const val INTERACTIVE_GQL_LEADERBOARD_DELAY = 3000L

        private const val START_COUNTDOWN_DELAY = 1000L

        private const val DEFAULT_BEFORE_LIVE_COUNT_DOWN = 5
    }
}