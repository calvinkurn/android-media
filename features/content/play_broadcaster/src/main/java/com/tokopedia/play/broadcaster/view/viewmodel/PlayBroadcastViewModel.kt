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
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoListener
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket
import com.tokopedia.play.broadcaster.socket.PlaySocketInfoListener
import com.tokopedia.play.broadcaster.socket.PlaySocketType
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.util.share.PlayShareWrapper
import com.tokopedia.play.broadcaster.view.state.BroadcastState
import com.tokopedia.play.broadcaster.view.state.BroadcastTimerState
import com.tokopedia.play_common.model.ui.PlayChatUiModel
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
        private val updateChannelUseCase: UpdateChannelUseCase,
        private val getSocketCredentialUseCase: GetSocketCredentialUseCase,
        private val dispatcher: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val playSocket: PlayBroadcastSocket
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
    val observableLiveDuration: LiveData<Event<BroadcastTimerState>>
        get() = _observableLiveDuration
    val observableLiveInfoState: LiveData<Event<BroadcastState>>
        get() = _observableLiveInfoState
    val observableLiveNetworkState: LiveData<Event<PlayPusherNetworkState>>
        get() = _observableLiveNetworkState
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
    val observableBannedEvent: LiveData<Event<BannedUiModel>>
        get() = _observableBannedEvent

    val shareContents: String
        get() = _observableShareInfo.value.orEmpty()

    private val _observableConfigInfo = MutableLiveData<NetworkResult<ConfigurationUiModel>>()
    private val _observableChannelInfo = MutableLiveData<NetworkResult<ChannelInfoUiModel>>()
    private val _observableTotalView = MutableLiveData<TotalViewUiModel>()
    private val _observableTotalLike = MutableLiveData<TotalLikeUiModel>()
    private val _observableLiveDuration = MutableLiveData<Event<BroadcastTimerState>>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableNewMetrics = MutableLiveData<Event<List<PlayMetricUiModel>>>()
    private val _observableShareInfo = MutableLiveData<String>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observableLiveNetworkState = MediatorLiveData<Event<PlayPusherNetworkState>>().apply {
        addSource(playPusher.getObservablePlayPusherNetworkState()) {
            value = Event(it)
        }
    }
    private val _observableLiveInfoState = MutableLiveData<Event<BroadcastState>>()
    private val _observableReportDuration = MutableLiveData<String>()
    private val _observableBannedEvent = MutableLiveData<Event<BannedUiModel>>()

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

            val configUiModel = PlayBroadcastUiMapper.mapConfiguration(config)
            setChannelId(configUiModel.channelId)

            if (configUiModel.channelType == ChannelType.Unknown) createChannel() // create channel when there are no channel exist
            if (configUiModel.channelType == ChannelType.Pause) { // get channel when channel status is paused
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
                playPusher.addStreamDuration(configUiModel.remainingTime)
            else playPusher.addStreamDuration(configUiModel.durationConfig.duration)

            playPusher.addMaxStreamDuration(configUiModel.durationConfig.duration)
            playPusher.addMaxPauseDuration(configUiModel.durationConfig.pauseDuration) // configure maximum pause duration

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
            val channelInfo = PlayBroadcastUiMapper.mapChannelInfo(channel)
            _observableChannelInfo.value = NetworkResult.Success(channelInfo)

            setChannelId(channelInfo.channelId)
            setChannelInfo(channelInfo)
            setSelectedProduct(PlayBroadcastUiMapper.mapChannelProductTags(channel.productTags))
            setSelectedCover(PlayBroadcastUiMapper.mapCover(getCurrentSetupDataStore().getSelectedCover(), channel.basic.coverUrl, channel.basic.title))

            generateShareLink(PlayBroadcastUiMapper.mapShareInfo(channel))

            null
        } catch (err: Throwable) {
            _observableChannelInfo.value = NetworkResult.Fail(err)
            err
        }
    }

    private suspend fun updateChannelStatus(status: PlayChannelStatus)  = withContext(dispatcher.io) {
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

    private suspend fun getSocketCredential() = withContext(dispatcher.io) {
        return@withContext getSocketCredentialUseCase.executeOnBackground()
    }

    /**
     * Apsara integration
     */
    fun initPushStream() {
        scope.launchCatchError(block = {
            playPusher.create()
            playPusher.addPusherInfoListener(object : PlayPusherInfoListener{
                override fun onTimerActive(remainingTime: String) {
                    _observableLiveDuration.value = Event(BroadcastTimerState.Active(remainingTime))
                }
                override fun onTimerAlmostFinish(minutesUntilFinished: Long) {
                    _observableLiveDuration.value = Event(BroadcastTimerState.AlmostFinish(minutesUntilFinished))
                }
                override fun onTimerFinish() {
                    _observableLiveDuration.value = Event(BroadcastTimerState.Finish)
                }
                override fun onReachMaximumPauseDuration() {
                    _observableLiveDuration.value = Event(BroadcastTimerState.ReachMaximumPauseDuration)
                }
                override fun onStop(timeElapsed: String) {
                    retrievedReportDuration(timeElapsed)
                }
            })
        }) {
        }
    }

    fun switchCamera() {
        scope.launchCatchError(block = {
            playPusher.switchCamera()
        }) {
        }
    }

    fun startPreview(surfaceView: SurfaceView) {
        scope.launchCatchError(block = {
            playPusher.startPreview(surfaceView)
        }) {
        }
    }

    fun stopPreview() {
        scope.launchCatchError(block = {
            playPusher.stopPreview()
        }) {
        }
    }

    fun startCountDown() {
        sharedPref.setNotFirstStreaming()
        _observableLiveInfoState.value = Event(BroadcastState.Init)
    }

    fun startPushStream() {
        scope.launchCatchError(block = {
            withContext(dispatcher.main) {
                startWebSocket()
                playPusher.startPush(ingestUrl)
                updateChannelStatus(PlayChannelStatus.Live)
            }
            _observableLiveInfoState.value = Event(BroadcastState.Start)
        }) {
            _observableLiveInfoState.value = Event(BroadcastState.Error(it,
                    onRetry = { this.startPushStream() }
            ))
        }
    }

    fun resumePushStream() {
        scope.launchCatchError(block = {
            withContext(dispatcher.io) {
                playPusher.resume()
                updateChannelStatus(PlayChannelStatus.Live)
            }
            _observableLiveInfoState.value = Event(BroadcastState.Start)
        }) {
        }
    }

    fun pausePushStream() {
        scope.launchCatchError(block = {
            withContext(dispatcher.io) {
                playPusher.pause()
                updateChannelStatus(PlayChannelStatus.Pause)
            }
            _observableLiveInfoState.value = Event(BroadcastState.Pause)
        }) {
        }
    }

    fun restartPushStream() {
        scope.launchCatchError(dispatcher.io, block = {
            playPusher.restartPush()
        }) {
            _observableLiveInfoState.value = Event(BroadcastState.Error(it,
                    onRetry = { this.restartPushStream() }
            ))
        }
    }

    fun stopPushStream(shouldNavigate: Boolean, reason: PlayChannelStatus = PlayChannelStatus.Stop) {
        scope.launchCatchError(block = {
            withContext(dispatcher.io) {
                playPusher.stopPush()
                playPusher.stopPreview()
                playSocket.destroy()
                updateChannelStatus(reason)
            }
            _observableLiveInfoState.value = Event(BroadcastState.Stop(shouldNavigate))
        }) {
            _observableLiveInfoState.value = Event(BroadcastState.Error(it,
                    onRetry = { this.stopPushStream(shouldNavigate, reason) }
            ))
        }
    }

    fun destroyPushStream() {
        playPusher.destroy()
        playSocket.destroy()
    }

    fun setChannelId(channelId: String) {
        hydraConfigStore.setChannelId(channelId)
    }

    private suspend fun startWebSocket() {
        val socketCredential = getSocketCredential()
        playSocket.config(socketCredential.setting.minReconnectDelay, socketCredential.setting.maxRetries, socketCredential.setting.pingInterval)

        fun connectWebSocket(): Job = scope.launch(dispatcher.io) {
            playSocket.connect(channelId = channelId, groupChatToken = socketCredential.gcToken)
            playSocket.socketInfoListener(object : PlaySocketInfoListener{
                override fun onReceive(data: PlaySocketType) {
                    when(data) {
                        is Metric -> queueNewMetrics(PlayBroadcastUiMapper.mapMetricList(data))
                        is TotalView -> _observableTotalView.value = PlayBroadcastUiMapper.mapTotalView(data)
                        is TotalLike -> _observableTotalLike.value = PlayBroadcastUiMapper.mapTotalLike(data)
                        is LiveDuration -> restartLiveDuration(data)
                        is ProductTagging -> setSelectedProduct(PlayBroadcastUiMapper.mapProductTag(data))
                        is Chat -> retrieveNewChat(PlayBroadcastUiMapper.mapIncomingChat(data))
                        is Banned -> retrieveBannedEvent(PlayBroadcastUiMapper.mapBannedEvent(data))
                    }
                }

                override fun onError(throwable: Throwable) {
                    connectWebSocket()
                }
            })
        }
        connectWebSocket()
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
            val durationUiModel = PlayBroadcastUiMapper.mapLiveDuration(duration)
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

    private fun retrievedReportDuration(timeElapsed: String) {
        scope.launch(dispatcher.main) {
            _observableReportDuration.value = timeElapsed
        }
    }

    private fun generateShareLink(shareUiModel: ShareUiModel) {
        scope.launch {
            PlayShareWrapper.generateShareLink(shareUiModel) {
                _observableShareInfo.value = it
            }
        }
    }

    private fun retrieveBannedEvent(data: BannedUiModel) {
        scope.launch(dispatcher.main) {
            _observableBannedEvent.value = Event(data)
        }
    }

    /**
     * mock
     */
    private fun mockChatList() {
        scope.launch(dispatcher.io) {
            while(isActive) {
                delay(3000)
                onRetrievedNewChat(
                    PlayBroadcastMocker.getMockChat()
                )
            }
        }
    }

    private fun mockMetrics() {
        scope.launch(dispatcher.io) {
            queueNewMetrics(
                    List(3) {
                        PlayBroadcastMocker.getMockMetric()
                    }
            )
        }
    }

    fun mockEventBanned() {
        scope.launch(dispatcher.io) {
            delay(20*1000)
            retrieveBannedEvent(PlayBroadcastMocker.mockEventBanned())
        }
    }
}