package com.tokopedia.play.broadcaster.view.viewmodel

import android.view.SurfaceView
import androidx.lifecycle.*
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.data.model.SerializableHydraSetupData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket
import com.tokopedia.play.broadcaster.socket.PlaySocketInfoListener
import com.tokopedia.play.broadcaster.socket.PlaySocketType
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.util.extension.convertMillisToMinuteSecond
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.util.state.PlayChannelLiveStateListener
import com.tokopedia.play.broadcaster.util.state.PlayLiveStateListener
import com.tokopedia.play.broadcaster.util.state.PlayLiveStateProcessor
import com.tokopedia.play.broadcaster.util.timer.PlayCountDownTimer
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherErrorState
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState
import com.tokopedia.play.broadcaster.view.state.PlayTimerState
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastViewModel @Inject constructor(
        livePusherBuilder: ApsaraLivePusherWrapper.Builder,
        livePusherStateProcessorFactory: PlayLiveStateProcessor.Factory,
        private val mDataStore: PlayBroadcastDataStore,
        private val hydraConfigStore: HydraConfigStore,
        private val sharedPref: HydraSharedPreferences,
        private val getConfigurationUseCase: GetConfigurationUseCase,
        private val getChannelUseCase: GetChannelUseCase,
        private val createChannelUseCase: CreateChannelUseCase,
        private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
        private val getAddedChannelTagsUseCase: GetAddedChannelTagsUseCase,
        private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
        private val dispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val playSocket: PlayBroadcastSocket,
        private val playBroadcastMapper: PlayBroadcastMapper,
        private val countDownTimer: PlayCountDownTimer
) : ViewModel() {

    val isFirstStreaming: Boolean
        get() = sharedPref.isFirstStreaming()

    val channelId: String
        get() = hydraConfigStore.getChannelId()
    private val ingestUrl: String
        get() = hydraConfigStore.getIngestUrl()
    val title: String
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
    val observableLiveDuration: LiveData<PlayTimerState>
        get() = _observableLiveDurationState
    val observableLiveInfoState: LiveData<PlayLivePusherState>
        get() = _observableLivePusherState
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

    private val _observableLivePusherState = MutableLiveData<PlayLivePusherState>()
    private val _observableLiveDurationState = MutableLiveData<PlayTimerState>()
    private val _observableEvent = MutableLiveData<EventUiModel>()

    private val livePusher = livePusherBuilder.build()

    private val liveStateListener = object : PlayLiveStateListener {
        override fun onStateChanged(state: PlayLivePusherState) {
            when (state) {
                is PlayLivePusherState.Start -> startWebSocket()
                is PlayLivePusherState.Resume -> if (state.isResumed) resumeTimer()
                is PlayLivePusherState.Pause -> countDownTimer.pause()
                else -> {}
            }

            if (state is PlayLivePusherState.Stop) {
                if (!state.isStopped) stopLiveStream(state.shouldNavigate)
            } else sendLivePusherState(state)
        }
    }

    private val channelLiveStateListener = object : PlayChannelLiveStateListener {
        override fun onChannelStateChanged(channelStatusType: PlayChannelStatusType) {
            updateChannelStatus(channelStatusType)
        }
    }

    private val countDownTimerListener = object : PlayCountDownTimer.Listener {
        override fun onCountDownActive(millis: Long) {
            _observableLiveDurationState.value = PlayTimerState.Active(millis.convertMillisToMinuteSecond())
        }

        override fun onCountDownAlmostFinish(minutes: Long) {
            _observableLiveDurationState.value = PlayTimerState.AlmostFinish(minutes)
        }

        override fun onCountDownFinish() {
            val event = _observableEvent.value
            if (event == null || (!event.freeze && !event.banned)) {
                _observableLiveDurationState.value = PlayTimerState.Finish
                stopLiveStream()
            }
        }
    }
    
    private val liveStateProcessor = livePusherStateProcessorFactory.create(livePusher, dispatcher, viewModelScope)
    private var isLiveStarted: Boolean = false

    init {
        _observableChatList.value = mutableListOf()
        liveStateProcessor.addStateListener(liveStateListener)
        liveStateProcessor.addStateListener(channelLiveStateListener)
        countDownTimer.setListener(countDownTimerListener)
    }

    override fun onCleared() {
        super.onCleared()
        liveStateProcessor.removeStateListener(liveStateListener)
        liveStateProcessor.removeStateListener(channelLiveStateListener)
        liveStateProcessor.onDestroy()
        countDownTimer.destroy()
    }

    fun getCurrentSetupDataStore(): PlayBroadcastSetupDataStore {
        return mDataStore.getSetupDataStore()
    }

    fun getConfiguration() {
        viewModelScope.launchCatchError(block = {
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

            _observableConfigInfo.value = NetworkResult.Success(configUiModel)

            setProductConfig(configUiModel.productTagConfig)
            setCoverConfig(configUiModel.coverConfig)
            setDurationConfig(configUiModel.durationConfig)
            setScheduleConfig(configUiModel.scheduleConfig)

            // configure live streaming duration
            if (configUiModel.channelType == ChannelType.Pause)
                countDownTimer.setDuration(configUiModel.remainingTime)
            else countDownTimer.setDuration(configUiModel.durationConfig.duration)

            countDownTimer.setMaxDuration(configUiModel.durationConfig.duration)
            liveStateProcessor.setPauseDuration(configUiModel.durationConfig.pauseDuration)

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

    private fun updateChannelStatus(status: PlayChannelStatusType) {
        viewModelScope.launchCatchError(block = {
            withContext(dispatcher.io) {
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
        }) {
        }
    }

    fun switchCamera() {
        livePusher.switchCamera()
    }

    fun startCameraPreview(surfaceView: SurfaceView) {
        livePusher.startPreview(surfaceView)
    }

    fun stopCameraPreview() {
        livePusher.stopPreview()
    }

    fun setFirstTimeLiveStreaming() {
        sharedPref.setNotFirstStreaming()
    }

    fun startLiveStream(withTimer: Boolean = true) {
        livePusher.start(ingestUrl)
        if (withTimer) startCountDownTimer()
        isLiveStarted = true
    }

    private fun reconnectLiveStream() {
        sendLivePusherState(PlayLivePusherState.Connecting)

        fun reconnectJob() {
            viewModelScope.launch {
                val err = getChannelDetail()
                if (err == null && _observableChannelInfo.value is NetworkResult.Success) {
                    val channelInfo = (_observableChannelInfo.value as NetworkResult.Success).data
                    when (channelInfo.status) {
                        PlayChannelStatusType.Pause,
                        PlayChannelStatusType.Live -> livePusher.reconnect()
                        else -> stopLiveStream(shouldNavigate = true)
                    }
                } else {
                    sendLivePusherState(
                            PlayLivePusherState.Error(
                                    PlayLivePusherErrorState.NetworkLoss,
                                    IllegalStateException("Failed to get channel details")
                            )
                    )
                    reconnectJob()
                }
            }
        }
        reconnectJob()
    }

    fun startCountDownTimer() {
        viewModelScope.launch {
            delay(1000)
            countDownTimer.start()
        }
    }

    fun continueLiveStream() {
        if (isLiveStarted) reconnectLiveStream()
        else startLiveStream()
    }

    fun stopLiveStream(shouldNavigate: Boolean = false) {
        playSocket.destroy()
        countDownTimer.stop()
        livePusher.stop()
        livePusher.stopPreview()
        sendLivePusherState(PlayLivePusherState.Stop(isStopped = true, shouldNavigate))
    }

    fun setChannelId(channelId: String) {
        hydraConfigStore.setChannelId(channelId)
    }

    fun checkShouldContinueLiveStream() {
        liveStateProcessor.onResume()
    }

    fun pauseLiveStream() {
        liveStateProcessor.onPause()
    }

    private fun sendLivePusherState(state: PlayLivePusherState) {
        viewModelScope.launch(dispatcher.io) {
            onLivePusherStateChanged(state)
        }
    }

    private suspend fun onLivePusherStateChanged(state: PlayLivePusherState) = withContext(dispatcher.main) {
        _observableLivePusherState.value = if (state is PlayLivePusherState.Error) state.copy(
                errorState = if (state.errorState is PlayLivePusherErrorState.ConnectFailed) {
                    state.errorState.copy(onRetry = { reconnectLiveStream() })
                } else state.errorState
        ) else state
    }

    private fun startWebSocket() {
        viewModelScope.launch {
            val socketCredential =  withContext(dispatcher.io) {
                return@withContext getSocketCredentialUseCase.executeOnBackground()
            }

            playSocket.config(socketCredential.setting.minReconnectDelay, socketCredential.setting.maxRetries, socketCredential.setting.pingInterval)

        fun connectWebSocket(): Job = viewModelScope.launch(dispatcher.io) {
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
                            if (_observableLiveDurationState.value !is PlayTimerState.Finish) {
                                val eventUiModel = playBroadcastMapper.mapFreezeEvent(data, _observableEvent.value)
                                if (eventUiModel.freeze) {
                                    stopLiveStream()
                                    _observableEvent.value = eventUiModel
                                }
                            }
                        }
                        is Banned -> {
                            if (_observableLiveDurationState.value !is PlayTimerState.Finish) {
                                val eventUiModel = playBroadcastMapper.mapBannedEvent(data, _observableEvent.value)
                                if (eventUiModel.banned) {
                                    stopLiveStream()
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
            val remainingDuration = duration.remaining*1000
            countDownTimer.restart(duration = remainingDuration)
        }) { }
    }

    private fun resumeTimer() {
        viewModelScope.launchCatchError(block = {
            countDownTimer.resume()
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
}