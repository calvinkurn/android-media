package com.tokopedia.play.broadcaster.view.viewmodel

import android.view.SurfaceView
import androidx.lifecycle.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.data.model.SerializableHydraSetupData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.PlayPusherInfoListener
import com.tokopedia.play.broadcaster.pusher.PlayPusherTimerListener
import com.tokopedia.play.broadcaster.pusher.apsara.ApsaraLivePusherErrorStatus
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket
import com.tokopedia.play.broadcaster.socket.PlaySocketInfoListener
import com.tokopedia.play.broadcaster.socket.PlaySocketType
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.state.LivePusherErrorStatus
import com.tokopedia.play.broadcaster.view.state.LivePusherState
import com.tokopedia.play.broadcaster.view.state.LivePusherTimerState
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastViewModel @Inject constructor(
        private val mDataStore: PlayBroadcastDataStore,
        private val hydraConfigStore: HydraConfigStore,
        private val sharedPref: HydraSharedPreferences,
        private val playPusher: PlayPusher,
        private val getConfigurationUseCase: GetConfigurationUseCase,
        private val getChannelUseCase: GetChannelUseCase,
        private val createChannelUseCase: CreateChannelUseCase,
        private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
        private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
        private val dispatcher: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val playSocket: PlayBroadcastSocket,
        private val playBroadcastMapper: PlayBroadcastMapper
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val isFirstStreaming: Boolean
        get() = sharedPref.isFirstStreaming()

    val channelId: String
        get() = hydraConfigStore.getChannelId()
    private val ingestUrl: String
        get() = hydraConfigStore.getIngestUrl()
    val title: String
        get() = hydraConfigStore.getTitle()

    val observableConfigInfo: LiveData<NetworkResult<ConfigurationUiModel>>
        get() = _observableConfigInfo
    val observableChannelInfo: LiveData<NetworkResult<ChannelInfoUiModel>>
        get() = _observableChannelInfo
    val observableTotalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val observableTotalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableLiveDuration: LiveData<LivePusherTimerState>
        get() = _observableLiveDurationState
    val observableLiveInfoState: LiveData<LivePusherState>
        get() = _observableLivePusherState
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableNewMetrics: LiveData<Event<List<PlayMetricUiModel>>>
        get() = _observableNewMetrics
    val observableProductList = Transformations.map(getCurrentSetupDataStore().getObservableSelectedProducts()) { dataList ->
        dataList.map { ProductContentUiModel.createFromData(it) }
    }
    val observableCover = getCurrentSetupDataStore().getObservableSelectedCover()
    val observableReportDuration: LiveData<String>
        get() = _observableReportDuration
    val observableEvent: LiveData<EventUiModel>
        get() = _observableEvent

    val shareContents: String
        get() = _observableShareInfo.value.orEmpty()

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

    private val _observableLivePusherState = MutableLiveData<LivePusherState>()
    private val _observableLiveDurationState = MutableLiveData<LivePusherTimerState>()
    private val _observableReportDuration = MutableLiveData<String>()
    private val _observableEvent = MutableLiveData<EventUiModel>()

    private var isManualStartTimer = false

    init {
        _observableChatList.value = mutableListOf()
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    fun getCurrentSetupDataStore(): PlayBroadcastSetupDataStore {
        return mDataStore.getSetupDataStore()
    }

    fun getConfiguration() {
        scope.launchCatchError(block = {
            _observableConfigInfo.value = NetworkResult.Loading

            val config = withContext(dispatcher.io) {
                getConfigurationUseCase.params = GetConfigurationUseCase.createParams(userSession.shopId)
                return@withContext getConfigurationUseCase.executeOnBackground()
            }

            val configUiModel = playBroadcastMapper.mapConfiguration(config)
            setChannelId(configUiModel.channelId)

            if (configUiModel.channelType == ChannelType.Unknown) createChannel() // create channel when there are no channel exist
            if (configUiModel.channelType == ChannelType.Pause // get channel when channel status is paused
                    || configUiModel.channelType == ChannelType.CompleteDraft) { // also when complete draft is true
                val err = getChannelById(configUiModel.channelId)
                if (err != null) {
                    throw err
                }
            }

            _observableReportDuration.value = configUiModel.timeElapsed
            _observableConfigInfo.value = NetworkResult.Success(configUiModel)

            setProductConfig(configUiModel.productTagConfig)
            setCoverConfig(configUiModel.coverConfig)
            setDurationConfig(configUiModel.durationConfig)

            // configure live streaming duration
            if (configUiModel.channelType == ChannelType.Pause)
                playPusher.setStreamDuration(configUiModel.remainingTime)
            else playPusher.setStreamDuration(configUiModel.durationConfig.duration)

            playPusher.setMaxStreamDuration(configUiModel.durationConfig.duration)
            playPusher.setMaxPauseDuration(configUiModel.durationConfig.pauseDuration) // configure maximum pause duration

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
            val channel =  withContext(dispatcher.io) {
                getChannelUseCase.params = GetChannelUseCase.createParams(channelId)
                return@withContext getChannelUseCase.executeOnBackground()
            }
            val channelInfo = playBroadcastMapper.mapChannelInfo(channel)
            _observableChannelInfo.value = NetworkResult.Success(channelInfo)

            setChannelId(channelInfo.channelId)
            setChannelInfo(channelInfo)
            setSelectedProduct(playBroadcastMapper.mapChannelProductTags(channel.productTags))
            setSelectedCover(playBroadcastMapper.mapCover(getCurrentSetupDataStore().getSelectedCover(), channel.basic.coverUrl, channel.basic.title))

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

    /**
     * Apsara integration
     */
    fun initPushStream() {
        playPusher.init()
        playPusher.setPlayPusherInfoListener(object : PlayPusherInfoListener {
            override fun onStarted() {
                scope.launchCatchError(block = {
                    updateChannelStatus(PlayChannelStatusType.Live)
                    _observableLivePusherState.value = LivePusherState.Started
                    if (!isManualStartTimer) playPusher.startTimer()
                    startWebSocket()
                }) {
                    _observableLivePusherState.value = LivePusherState.Error(LivePusherErrorStatus.ConnectFailed {
                        startPushStream()
                    })
                    playPusher.stopPush()
                    playPusher.pauseTimer()
                }
            }

            override fun onResumed() {
                scope.launchCatchError(block = {
                    updateChannelStatus(PlayChannelStatusType.Live)
                    _observableLivePusherState.value = LivePusherState.Started
                    playPusher.resumeTimer()
                }) {
                    _observableLivePusherState.value = LivePusherState.Error(LivePusherErrorStatus.ConnectFailed {
                        _observableLivePusherState.value = LivePusherState.Connecting
                        resumePushStream()
                    })
                    playPusher.stopPush()
                    playPusher.pauseTimer()
                }
            }

            override fun onPaused() {
                scope.launchCatchError(block = {
                    updateChannelStatus(PlayChannelStatusType.Pause)
                }) {
                }
            }

            override fun onRecovered() {
                autoReconnectPushStream()
            }

            override fun onError(errorStatus: ApsaraLivePusherErrorStatus) {
               val liveErrorStatus =  when (errorStatus) {
                   ApsaraLivePusherErrorStatus.NetworkPoor -> LivePusherErrorStatus.NetworkPoor
                   ApsaraLivePusherErrorStatus.NetworkLoss,
                   ApsaraLivePusherErrorStatus.ReconnectFailed -> LivePusherErrorStatus.NetworkLoss
                   ApsaraLivePusherErrorStatus.ConnectFailed -> LivePusherErrorStatus.ConnectFailed {
                       reconnectPushStream()
                   }
                   ApsaraLivePusherErrorStatus.SystemError -> LivePusherErrorStatus.UnRecoverable {
                       playPusher.restartPush()
                   }
               }
                _observableLivePusherState.value = LivePusherState.Error(liveErrorStatus)
            }

        })
        playPusher.setPlayPusherTimerListener(object : PlayPusherTimerListener {
            override fun onCountDownActive(timeLeft: String) {
                _observableLiveDurationState.value = LivePusherTimerState.Active(timeLeft)
            }

            override fun onCountDownAlmostFinish(minutesUntilFinished: Long) {
                _observableLiveDurationState.value = LivePusherTimerState.AlmostFinish(minutesUntilFinished)
            }

            override fun onCountDownFinish() {
                val event = _observableEvent.value
                if (event == null || (!event.freeze && !event.banned)) {
                    _observableLiveDurationState.value = LivePusherTimerState.Finish
                    stopPushStream()
                }
            }

            override fun onReachMaximumPauseDuration() {
                stopPushStream()
            }

        })
    }

    fun switchCamera() {
        playPusher.switchCamera()
    }

    fun startPreview(surfaceView: SurfaceView) {
        playPusher.startPreview(surfaceView)
    }

    fun stopPreview() {
        playPusher.stopPreview()
    }

    fun setFirstTimeLiveStreaming() {
        sharedPref.setNotFirstStreaming()
    }

    fun startPushStream(manualStartTimer: Boolean = false) {
        _observableLivePusherState.value = LivePusherState.Connecting
        playPusher.startPush(ingestUrl)
        this.isManualStartTimer = manualStartTimer
    }

    private fun reconnectPushStream() {
        _observableLivePusherState.value = LivePusherState.Connecting
        scope.launch {
            val err = getChannelDetail()
            if (err == null) {
                playPusher.startPush(ingestUrl)
            } else {
                _observableLivePusherState.value = LivePusherState.Error(LivePusherErrorStatus.ConnectFailed {
                    reconnectPushStream()
                })
            }
        }
    }

    private fun autoReconnectPushStream() {
        _observableLivePusherState.value = LivePusherState.Connecting
        scope.launch {
            val err = getChannelDetail()
            if (err == null && _observableChannelInfo.value is NetworkResult.Success) {
                val channelInfo = (_observableChannelInfo.value as NetworkResult.Success).data
                when (channelInfo.status) {
                    PlayChannelStatusType.Pause -> {
                        pausePushStream()
                        playPusher.pauseTimer()
                        _observableLivePusherState.value = LivePusherState.Paused
                    }
                    PlayChannelStatusType.Live ->  _observableLivePusherState.value = LivePusherState.Recovered
                    else -> stopPushStream(shouldNavigate = true)
                }
            } else {
                _observableLivePusherState.value = LivePusherState.Error(LivePusherErrorStatus.ConnectFailed {
                    autoReconnectPushStream()
                })
            }
        }
    }

    fun startTimer() {
        scope.launch {
            delay(1000)
            playPusher.startTimer()
        }
    }

    fun resumePushStream() {
        playPusher.resumePush()
    }

    fun pausePushStream() {
        playPusher.pausePush()
    }

    fun stopPushStream(shouldNavigate: Boolean = false) {
        scope.launchCatchError(block = {
            withContext(dispatcher.io) {
                playSocket.destroy()
                playPusher.stopTimer()
                playPusher.stopPush()
                playPusher.stopPreview()
                updateChannelStatus(PlayChannelStatusType.Stop)
            }
            _observableLivePusherState.value = LivePusherState.Stopped(shouldNavigate)
        }) {
            _observableLivePusherState.value = LivePusherState.Stopped(shouldNavigate)
        }
    }

    fun destroyPushStream() {
        playPusher.stopPush()
        playPusher.destroy()
        playSocket.destroy()
    }

    fun setChannelId(channelId: String) {
        hydraConfigStore.setChannelId(channelId)
    }

    fun getReportDuration() {
        scope.launch {
            val liveDuration = playPusher.getTimeElapsed()
            _observableReportDuration.value = liveDuration
        }
    }

    private fun startWebSocket() {
        scope.launch {
            val socketCredential =  withContext(dispatcher.io) {
                return@withContext getSocketCredentialUseCase.executeOnBackground()
            }

            playSocket.config(socketCredential.setting.minReconnectDelay, socketCredential.setting.maxRetries, socketCredential.setting.pingInterval)

        fun connectWebSocket(): Job = scope.launch(dispatcher.io) {
            playSocket.connect(channelId = channelId, groupChatToken = socketCredential.gcToken)
            playSocket.socketInfoListener(object : PlaySocketInfoListener{
                override fun onReceive(data: PlaySocketType) {
                    when(data) {
                        is NewMetricList -> queueNewMetrics(playBroadcastMapper.mapNewMetricList(data))
                        is TotalView -> _observableTotalView.value = playBroadcastMapper.mapTotalView(data)
                        is TotalLike -> _observableTotalLike.value = playBroadcastMapper.mapTotalLike(data)
                        is LiveDuration -> restartLiveDuration(data)
                        is ProductTagging -> setSelectedProduct(playBroadcastMapper.mapProductTag(data))
                        is Chat -> retrieveNewChat(playBroadcastMapper.mapIncomingChat(data))
                        is Freeze -> {
                            if (_observableLiveDurationState.value !is LivePusherTimerState.Finish) {
                                val eventUiModel = playBroadcastMapper.mapFreezeEvent(data, _observableEvent.value)
                                if (eventUiModel.freeze) {
                                    stopPushStream()
                                    _observableEvent.value = eventUiModel
                                }
                            }
                        }
                        is Banned -> {
                            if (_observableLiveDurationState.value !is LivePusherTimerState.Finish) {
                                val eventUiModel = playBroadcastMapper.mapBannedEvent(data, _observableEvent.value)
                                if (eventUiModel.banned) {
                                    stopPushStream()
                                    _observableEvent.value = eventUiModel
                                }
                            }
                        }
                    }
                }

                    override fun onError(throwable: Throwable) {
                        connectWebSocket()
                    }
                })
            }
            connectWebSocket()
        }
    }

    private fun setSelectedProduct(products: List<ProductData>) {
        getCurrentSetupDataStore().setSelectedProducts(products)
    }

    private fun setSelectedCover(cover: PlayCoverUiModel) {
        getCurrentSetupDataStore().setFullCover(cover)
    }

    private fun setChannelInfo(channelInfo: ChannelInfoUiModel) {
        hydraConfigStore.setIngestUrl(channelInfo.ingestUrl)
        hydraConfigStore.setTitle(channelInfo.title)
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

    private fun restartLiveDuration(duration: LiveDuration) {
        scope.launchCatchError(block = {
            val durationUiModel = playBroadcastMapper.mapLiveDuration(duration)
            playPusher.restartStreamDuration(durationUiModel.remaining)
        }) { }
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
        scope.launch(dispatcher.main) {
            _observableNewMetrics.value = Event(metricList)
        }
    }

    private fun retrieveNewChat(newChat: PlayChatUiModel) {
        scope.launch(dispatcher.io) {
            onRetrievedNewChat(newChat)
        }
    }

    private fun generateShareLink(shareUiModel: ShareUiModel) {
        scope.launch {
            PlayShareWrapper.generateShareLink(shareUiModel) {
                _observableShareInfo.value = it
            }
        }
    }
}