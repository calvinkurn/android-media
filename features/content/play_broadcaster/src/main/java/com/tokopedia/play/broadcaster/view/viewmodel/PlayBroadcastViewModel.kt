package com.tokopedia.play.broadcaster.view.viewmodel

import android.Manifest
import androidx.lifecycle.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.model.ConcurrentUser
import com.tokopedia.play.broadcaster.domain.model.LiveStats
import com.tokopedia.play.broadcaster.domain.model.Metric
import com.tokopedia.play.broadcaster.domain.usecase.CreateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
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
    val observableChannelInfo: LiveData<ChannelInfoUiModel>
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
    val observableProductList: LiveData<List<ProductContentUiModel>>
        get() = _observableProductList

    val shareInfo: ShareUiModel?
        get() = _observableShareInfo.value

    private val _observableConfigInfo = MutableLiveData<NetworkResult<ConfigurationUiModel>>()
    private val _observableChannelInfo = MutableLiveData<ChannelInfoUiModel>()
    private val _observableCreateChannel = MutableLiveData<NetworkResult<String>>()
    private val _observableTotalView = MutableLiveData<TotalViewUiModel>()
    private val _observableTotalLike = MutableLiveData<TotalLikeUiModel>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableNewMetric = MutableLiveData<Event<PlayMetricUiModel>>()
    private val _observableProductList = MutableLiveData<List<ProductContentUiModel>>()
    private val _observableShareInfo = MutableLiveData<ShareUiModel>()
    private val _observableNewChat = MediatorLiveData<Event<PlayChatUiModel>>().apply {
        addSource(_observableChatList) { chatList ->
            chatList.lastOrNull()?.let { value = Event(it) }
        }
    }
    private val _observableChannelId: LiveData<String> = MediatorLiveData<String>().apply {
        addSource(_observableConfigInfo) {
            value = if (it is NetworkResult.Success) it.data.channelId else ""
        }
        addSource(_observableCreateChannel) {
            value = if (it is NetworkResult.Success) it.data else ""
        }
        addSource(_observableChannelInfo) {
            value = it.channelId
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

    init {
        socketResponseHandler.observeForever(socketResponseHandlerObserver)

        mockChatList()
        mockMetrics()
        mockProductList()
        mockShareData()
    }

    override fun onCleared() {
        super.onCleared()
        socketResponseHandler.removeObserver(socketResponseHandlerObserver)
        playSocket.destroy()
        scope.cancel()
    }

    fun getCurrentSetupDataStore(): PlayBroadcastSetupDataStore {
        return mDataStore.getSetupDataStore()
    }

    fun getConfiguration() {
        scope.launchCatchError(block = {
            _observableConfigInfo.value = NetworkResult.Loading

//            val config = withContext(dispatcher.io) {
//                getConfigurationUseCase.params = GetConfigurationUseCase.createParams(userSession.shopId)
//                return@withContext getConfigurationUseCase.executeOnBackground()
//            }
//            val configUiModel = PlayBroadcastUiMapper.mapConfiguration(config)
            delay(3000)
            val configUiModel = PlayBroadcastMocker.getMockConfigurationDraftChannel()

            launch {
                if (configUiModel.channelStatus == PlayChannelStatus.Unknown) createChannel()
                else getChannel(configUiModel.channelId)
            }

            _observableConfigInfo.value = NetworkResult.Success(configUiModel)
            // configure maximum pause duration
            playPusher.addMaxPauseDuration(configUiModel.durationConfig.pauseDuration)

        }) {
            _observableConfigInfo.value = NetworkResult.Fail(it) { getConfiguration() }
        }
    }

    private suspend fun createChannel() = withContext(dispatcher.io) {
        val channelId = createChannelUseCase.apply {
            params = CreateChannelUseCase.createParams(
                    authorId = userSession.shopId
            )
        }.executeOnBackground()

        _observableCreateChannel.value = NetworkResult.Success(channelId.id)
    }

    private suspend fun getChannel(channelId: String) = withContext(dispatcher.io) {
        val channel = getChannelUseCase.apply {
            params = GetChannelUseCase.createParams(
                    channelId = channelId
            )
        }.executeOnBackground()

        _observableChannelInfo.value = PlayBroadcastUiMapper.mapChannelInfo(channel)
        _observableProductList.value = PlayBroadcastUiMapper.mapProductList(channel.productTags)
        _observableShareInfo.value = PlayBroadcastUiMapper.mapShareInfo(channel)
    }

    private fun updateChannelStatus(status: PlayChannelStatus) {
        // TODO("update channel status, still waiting for the API to finish")
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
        playPusher.create()
    }

    fun startPushStream(ingestUrl: String) {
        scope.launch {
            if (ingestUrl.isNotEmpty() && allPermissionGranted()) {
                startWebSocket()
                updateChannelStatus(PlayChannelStatus.Active)
                playPusher.startPush(ingestUrl)
            }
        }
    }

    fun resumePushStream() {
        scope.launch {
            if (!playPusher.isPushing() && allPermissionGranted()) {
                updateChannelStatus(PlayChannelStatus.Active)
                playPusher.resume()
            }
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

    private fun mockProductList() {
        scope.launch(dispatcher.io) {
            delay(3000)
            _observableProductList.postValue(
                    PlayBroadcastMocker.getMockProductList(5)
            )
        }
    }

    private fun mockShareData() {
        scope.launch(dispatcher.io) {
            delay(3000)
            _observableShareInfo.postValue(PlayBroadcastMocker.getMockShare())
        }
    }
}