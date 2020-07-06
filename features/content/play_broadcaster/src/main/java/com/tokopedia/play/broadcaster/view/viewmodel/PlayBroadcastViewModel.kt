package com.tokopedia.play.broadcaster.view.viewmodel

import android.Manifest
import android.view.SurfaceView
import androidx.lifecycle.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket
import com.tokopedia.play.broadcaster.socket.PlaySocketInfoListener
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.error.PusherErrorThrowable
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionState
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionUtil
import com.tokopedia.play.broadcaster.view.state.BroadcastState
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
        private val playPusher: PlayPusher,
        private val permissionUtil: PlayPermissionUtil,
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

    private val channelId: String
        get() = hydraConfigStore.getChannelId()
    private val ingestUrl: String
        get() = hydraConfigStore.getIngestUrl()

    val observableConfigInfo: LiveData<NetworkResult<ConfigurationUiModel>>
        get() = _observableConfigInfo
    val observableChannelInfo: LiveData<NetworkResult<ChannelInfoUiModel>>
        get() = _observableChannelInfo
    val observableTotalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val observableTotalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableLiveDuration: LiveData<DurationUiModel>
        get() = _observableLiveDuration
    val observableLiveInfoState: LiveData<Event<BroadcastState>>
        get() = _observableLiveInfoState
    val observableLiveNetworkState: LiveData<Event<PlayPusherNetworkState>>
        get() = _observableLiveNetworkState
    val observablePermissionState: LiveData<PlayPermissionState>
        get() = _observablePermissionState
    val observableChatList: LiveData<out List<PlayChatUiModel>>
        get() = _observableChatList
    val observableNewChat: LiveData<Event<PlayChatUiModel>>
        get() = _observableNewChat
    val observableNewMetric: LiveData<Event<PlayMetricUiModel>>
        get() = _observableNewMetric
    val observableProductList = Transformations.map(getCurrentSetupDataStore().getObservableSelectedProducts()) { dataList ->
        dataList.map { ProductContentUiModel.createFromData(it) }
    }
    val observableCover = getCurrentSetupDataStore().getObservableSelectedCover()

    val shareInfo: ShareUiModel?
        get() = _observableShareInfo.value

    private val _observableConfigInfo = MutableLiveData<NetworkResult<ConfigurationUiModel>>()
    private val _observableChannelInfo = MutableLiveData<NetworkResult<ChannelInfoUiModel>>()
    private val _observableTotalView = MutableLiveData<TotalViewUiModel>()
    private val _observableTotalLike = MutableLiveData<TotalLikeUiModel>()
    private val _observableLiveDuration = MutableLiveData<DurationUiModel>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableNewMetric = MutableLiveData<Event<PlayMetricUiModel>>()
    private val _observableShareInfo = MutableLiveData<ShareUiModel>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observableChannelId: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(_observableConfigInfo) {
            if (it is NetworkResult.Success) setChannelId(it.data.channelId)
        }
        addSource(_observableChannelInfo) {
            if (it is NetworkResult.Success) setChannelId(it.data.channelId)
        }
    }

    private val _observableLiveNetworkState = MediatorLiveData<Event<PlayPusherNetworkState>>().apply {
        addSource(playPusher.getObservablePlayPusherNetworkState()) {
            value = Event(it)
        }
    }
    private val _observableLiveInfoState = MediatorLiveData<Event<BroadcastState>>().apply {
        addSource(playPusher.getObservablePlayPusherInfoState()) {
            if (it is PlayPusherInfoState.Error) value = Event(BroadcastState.Error(PusherErrorThrowable(it.errorType)))
        }
    }
    private val _observablePermissionState = permissionUtil.getObservablePlayPermissionState()
    private val socketResponseHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(playSocket.getObservablePlaySocketMessage()) {
            when(it) {
                is Metric -> onRetrievedNewMetric(PlayBroadcastUiMapper.mapMetricList(it))
                is TotalView -> _observableTotalView.value = PlayBroadcastUiMapper.mapTotalView(it)
                is TotalLike -> _observableTotalLike.value = PlayBroadcastUiMapper.mapTotalLike(it)
                is LiveDuration -> _observableLiveDuration.value = PlayBroadcastUiMapper.mapLiveDuration(it)
            }
        }
    }
    private val socketResponseHandlerObserver = object : Observer<Unit> {
        override fun onChanged(t: Unit?) {}
    }

    private val channelIdObserver = object : Observer<String> {
        override fun onChanged(t: String?) {}
    }

    init {
        socketResponseHandler.observeForever(socketResponseHandlerObserver)
        _observableChannelId.observeForever(channelIdObserver)

        _observableChatList.value = mutableListOf()
    }

    override fun onCleared() {
        super.onCleared()
        socketResponseHandler.removeObserver(socketResponseHandlerObserver)
        _observableChannelId.removeObserver(channelIdObserver)
        playSocket.destroy()
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
            if (configUiModel.channelType == ChannelType.Unknown) createChannel() // create channel when there are no channel exist
            else if (configUiModel.channelType == ChannelType.Pause) getChannelById(configUiModel.channelId) // get channel when channel status is paused
            _observableConfigInfo.value = NetworkResult.Success(configUiModel)
            setMaxMinProduct(configUiModel.productTagConfig)
            playPusher.addMaxPauseDuration(configUiModel.durationConfig.pauseDuration) // configure maximum pause duration
        }) {
            _observableConfigInfo.value = NetworkResult.Fail(it) { this.getConfiguration() }
        }
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
            _observableShareInfo.value = PlayBroadcastUiMapper.mapShareInfo(channel)

            setIngestUrl(channelInfo.ingestUrl)
            setSelectedProduct(PlayBroadcastUiMapper.mapChannelProductTags(channel.productTags))
            setSelectedCover(PlayBroadcastUiMapper.mapCover(getCurrentSetupDataStore().getSelectedCover(), channel.basic.coverUrl, channel.basic.title))

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
     * Permission
     */
    fun checkPermission() {
        permissionUtil.checkPermission(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO))
    }

    fun allPermissionGranted() = permissionUtil.isAllPermissionGranted()

    fun getPermissionUtil(): PlayPermissionUtil {
        return permissionUtil
    }

    /**
     * Apsara integration
     */
    fun initPushStream() {
        scope.launchCatchError(block = {
            playPusher.create()
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

    fun startCountDown() {
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
            _observableLiveInfoState.value = Event(BroadcastState.Error(it))
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
            _observableLiveInfoState.value = Event(BroadcastState.Error(it))
        }
    }

    fun stopPushStream() {
        scope.launchCatchError(block = {
            withContext(dispatcher.io) {
                playPusher.stopPush()
                playPusher.stopPreview()
                playSocket.destroy()
                updateChannelStatus(PlayChannelStatus.Stop)
            }
            _observableLiveInfoState.value = Event(BroadcastState.Stop)
        }) {
            _observableLiveInfoState.value = Event(BroadcastState.Error(it))
        }
    }

    fun destroyPushStream() {
        scope.launchCatchError(dispatcher.io, block = {
            playPusher.destroy()
            playSocket.destroy()
        }) {
            _observableLiveInfoState.value = Event(BroadcastState.Error(it))
        }
    }

    private suspend fun startWebSocket() {
        val socketCredential = getSocketCredential()
        playSocket.config(socketCredential.setting.minReconnectDelay, socketCredential.setting.maxRetries, socketCredential.setting.pingInterval)

        fun connectWebSocket(): Job = scope.launch {
            playSocket.connect(channelId = channelId, groupChatToken = socketCredential.gcToken)
            playSocket.socketInfoListener(object : PlaySocketInfoListener{
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

    private fun setChannelId(channelId: String) {
        hydraConfigStore.setChannelId(channelId)
    }

    private fun setIngestUrl(ingestUrl: String) {
        hydraConfigStore.setIngestUrl(ingestUrl)
    }

    private fun setMaxMinProduct(configModel: ProductTagConfigUiModel) {
        hydraConfigStore.setMaxProduct(configModel.maxProduct)
        hydraConfigStore.setMinProduct(configModel.minProduct)
    }

    /**
     * UI
     */
    private suspend fun onRetrievedNewChat(newChat: PlayChatUiModel) = withContext(dispatcher.main) {
        val currentChatList = _observableChatList.value ?: mutableListOf()
        currentChatList.add(newChat)
        _observableChatList.value = currentChatList
    }

    private suspend fun onRetrievedNewMetric(newMetric: PlayMetricUiModel) = withContext(dispatcher.main) {
        _observableNewMetric.value = Event(newMetric)
    }

    private fun onRetrievedNewMetric(metricList: MutableList<PlayMetricUiModel>) {
        scope.launch(dispatcher.io) {
            for (metric in  metricList) {
                delay(metric.interval)
                onRetrievedNewMetric(metric)
                metricList.remove(metric)
            }
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
            while(isActive) {
                delay(3000)
                onRetrievedNewMetric(
                    PlayBroadcastMocker.getMockMetric()
                )
            }
        }
    }
}