package com.tokopedia.play.broadcaster.view.viewmodel

import android.Manifest
import androidx.lifecycle.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.domain.model.ConcurrentUser
import com.tokopedia.play.broadcaster.domain.model.LiveStats
import com.tokopedia.play.broadcaster.domain.model.Metric
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
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionState
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionUtil
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

    val channelId: String
        get() = _observableChannelId.value?: throw IllegalStateException("Channel ID has not been retrieved")

    val observableConfigInfo: LiveData<NetworkResult<ConfigurationUiModel>>
        get() = _observableConfigInfo
    val observableChannelInfo: LiveData<NetworkResult<ChannelInfoUiModel>>
        get() = _observableChannelInfo
    val observableTotalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val observableTotalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableLiveInfoState: LiveData<Event<PlayPusherInfoState>>
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
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableNewMetric = MutableLiveData<Event<PlayMetricUiModel>>()
    private val _observableShareInfo = MutableLiveData<ShareUiModel>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observableChannelId: MutableLiveData<String> = MediatorLiveData<String>().apply {
        addSource(_observableConfigInfo) {
            if (it is NetworkResult.Success) value = it.data.channelId
        }
        addSource(_observableChannelInfo) {
            if (it is NetworkResult.Success) value = it.data.channelId
        }
    }

    private val _observableLiveNetworkState = MediatorLiveData<Event<PlayPusherNetworkState>>().apply {
        addSource(playPusher.getObservablePlayPusherNetworkState()) {
            value = Event(it)
        }
    }
    private val _observableLiveInfoState = MediatorLiveData<Event<PlayPusherInfoState>>().apply {
        addSource(playPusher.getObservablePlayPusherInfoState()) {
            value = Event(it)
        }
    }
    private val _observablePermissionState = permissionUtil.getObservablePlayPermissionState()
    private val socketResponseHandler: LiveData<Unit> = MediatorLiveData<Unit>().apply {
        addSource(playSocket.getObservablePlaySocketMessage()) {
            when(it) {
                is Metric -> onRetrievedNewMetric(PlayBroadcastUiMapper.mapMetricList(it))
                is ConcurrentUser -> _observableTotalView.value = PlayBroadcastUiMapper.mapTotalView(it)
                is LiveStats -> _observableTotalLike.value = PlayBroadcastUiMapper.mapTotalLike(it)
            }
            // TODO("retrieve & update count down")
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
        initPushStream()

        mockChatList()
        mockMetrics()
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
//            delay(2000)
//            val configUiModel = PlayBroadcastMocker.getMockConfigurationDraftChannel() // TODO remove mock
            if (configUiModel.channelType == ChannelType.Unknown) createChannel() // create channel when there are no channel exist
            _observableConfigInfo.value = NetworkResult.Success(configUiModel)
            playPusher.addMaxPauseDuration(configUiModel.durationConfig.pauseDuration) // configure maximum pause duration
        }) {
            _observableConfigInfo.value = NetworkResult.Fail(it) { this.getConfiguration() }
        }
    }

    fun fetchChannelData() {
        scope.launch {
            getChannel(channelId)
        }
    }

    private suspend fun createChannel() {
        val channelId = withContext(dispatcher.io) {
            createChannelUseCase.params = CreateChannelUseCase.createParams(
                    authorId = userSession.shopId
            )
            return@withContext createChannelUseCase.executeOnBackground()
        }
        _observableChannelId.value = channelId.id
    }

    private suspend fun getChannel(channelId: String) {
        _observableChannelInfo.value = NetworkResult.Loading
        try {
            val channel =  withContext(dispatcher.io) {
                getChannelUseCase.params = GetChannelUseCase.createParams(channelId)
                return@withContext getChannelUseCase.executeOnBackground()
            }
            _observableChannelInfo.value = NetworkResult.Success(PlayBroadcastUiMapper.mapChannelInfo(channel))
//            _observableShareInfo.value = PlayBroadcastUiMapper.mapShareInfo(channel)
            _observableShareInfo.value = PlayBroadcastMocker.getMockShare()

            //TODO("If channel data is not mocked from backend, uncomment this")
//            setSelectedProduct(PlayBroadcastUiMapper.mapProductListToData(channel.productTags))
//            setSelectedCover(PlayBroadcastUiMapper.mapCover(getCurrentSetupDataStore().getSelectedCover(), channel.basic.coverUrl, channel.basic.title))

        } catch (e: Throwable) {
            _observableChannelInfo.value = NetworkResult.Fail(e)
        }
    }

    private suspend fun updateChannelStatus(status: PlayChannelStatus)  = withContext(dispatcher.io) {
        updateChannelUseCase.apply {
            params = UpdateChannelUseCase.createParams(
                    channelId = channelId,
                    authorId = userSession.shopId,
                    status = status
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
    private fun initPushStream() {
        playPusher.create()
    }

    fun startPushStream(ingestUrl: String) {
        if (ingestUrl.isEmpty() || !allPermissionGranted()) return
        scope.launchCatchError(block = {
            startWebSocket()
            updateChannelStatus(PlayChannelStatus.Live)
            playPusher.startPush(ingestUrl)
        }) {

        }
    }

    fun resumePushStream() {
        if (playPusher.isPushing() || !allPermissionGranted()) return
        scope.launchCatchError(block = {
            updateChannelStatus(PlayChannelStatus.Live)
            playPusher.resume()
        }) {

        }
    }

    fun pausePushStream() {
        scope.launch {
            if (playPusher.isPushing()) {
                updateChannelStatus(PlayChannelStatus.Pause)
                playPusher.pause()
            }
        }
    }

    fun stopPushStream() {
        scope.launch {
            updateChannelStatus(PlayChannelStatus.Stop)
            playPusher.stopPush()
            playPusher.stopPreview()
            playSocket.destroy()
        }
    }

    fun getPlayPusher(): PlayPusher {
        return playPusher
    }

    private fun startWebSocket() {
        playSocket.connect(channelId = "", groupChatToken = "")
        playSocket.socketInfoListener(object : PlaySocketInfoListener{
            override fun onError(throwable: Throwable) {
                // TODO("reconnect socket")
            }
        })
    }

    private fun setSelectedProduct(products: List<ProductData>) {
        getCurrentSetupDataStore().setSelectedProducts(products)
    }

    private fun setSelectedCover(cover: PlayCoverUiModel) {
        getCurrentSetupDataStore().setFullCover(cover)
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
                delay(1000)
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