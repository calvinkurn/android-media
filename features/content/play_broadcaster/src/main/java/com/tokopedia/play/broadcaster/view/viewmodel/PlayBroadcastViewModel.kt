package com.tokopedia.play.broadcaster.view.viewmodel

import android.Manifest
import androidx.lifecycle.*
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.model.ConcurrentUser
import com.tokopedia.play.broadcaster.domain.model.LiveStats
import com.tokopedia.play.broadcaster.domain.model.Metric
import com.tokopedia.play.broadcaster.domain.usecase.CreateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.socket.PlayBroadcastSocket
import com.tokopedia.play.broadcaster.socket.PlaySocketInfoListener
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.*
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
        private val createChannelUseCase: CreateChannelUseCase,
        private val dispatcher: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface,
        private val playSocket: PlayBroadcastSocket
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val channelId: String
        get() = observableConfigInfo.value?.channelId?.toString() ?: throw IllegalStateException("Channel ID has not been retrieved")

    val observableConfigInfo: LiveData<ConfigurationUiModel>
        get() = _observableConfigInfo
    val observableChannelInfo: LiveData<ChannelInfoUiModel>
        get() = _observableChannelInfo
    val observableTotalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val observableTotalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableLiveInfoState: LiveData<PlayPusherInfoState>
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
    val observableShareInfo: LiveData<ShareUiModel>
        get() = _observableShareInfo

    val configuration: ConfigurationUiModel?
        get() = _observableConfigInfo.value
    val shareInfo: ShareUiModel?
        get() = _observableShareInfo.value

    private val _observableConfigInfo = MutableLiveData<ConfigurationUiModel>()
    private val _observableChannelInfo = MutableLiveData<ChannelInfoUiModel>()
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
    private val _observableLiveNetworkState = MediatorLiveData<Event<PlayPusherNetworkState>>().apply {
        addSource(playPusher.getObservablePlayPusherNetworkState()) {
            value = Event(it)
        }
    }
    private val _observableLiveInfoState = playPusher.getObservablePlayPusherInfoState()
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
        permissionUtil.checkPermission(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO))
        playPusher.create()
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
        scope.launch {
            val configurationUiModel = PlayBroadcastMocker.getMockConfiguration()
            _observableConfigInfo.value = configurationUiModel

            // TODO("tidying later, still waiting for the API to finish")
            if (configurationUiModel.haveOnGoingLive) {
                getChannel(configurationUiModel.channelId)
            } else {
                if (configurationUiModel.channelId == 0)
                    createChannel()
            }

            // TODO("match local countdown timer with socket")
            playPusher.addMaxStreamDuration(configurationUiModel.durationConfig.duration)
            playPusher.addMaxPauseDuration(configurationUiModel.durationConfig.pauseDuration)
        }
    }

    private suspend fun createChannel() = withContext(dispatcher.io) {
        return@withContext createChannelUseCase.apply {
            params = CreateChannelUseCase.createParams(
                    authorId = userSession.shopId
            )
        }.executeOnBackground()
    }

    // TODO("get channel, still waiting for the API to finish")
    fun getChannel(channelId: Int): ChannelInfoUiModel {
        val channelInfo = PlayBroadcastMocker.getMockUnStartedChannel()
        _observableChannelInfo.value = channelInfo
        _observableTotalView.value = PlayBroadcastMocker.getMockTotalView()
        _observableTotalLike.value = PlayBroadcastMocker.getMockTotalLike()
        return channelInfo
    }

    private fun updateChannelStatus(status: PlayChannelStatus) {
        // TODO("update channel status, still waiting for the API to finish")
    }

    /**
     * Apsara integration
     */
    fun startPushBroadcast(ingestUrl: String) {
        scope.launch {
            if (ingestUrl.isNotEmpty()) {
                startWebSocket()
                updateChannelStatus(PlayChannelStatus.Active)
                playPusher.startPush(ingestUrl)
            }
        }
    }

    fun resumePushStream() {
        scope.launch {
            if (!playPusher.isPushing()) {
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

    fun stopPushBroadcast() {
        scope.launch {
            updateChannelStatus(PlayChannelStatus.Finish)
            playPusher.stopPush()
            playPusher.stopPreview()
            playSocket.destroy()
        }
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

    fun getPlayPusher(): PlayPusher {
        return playPusher
    }

    fun getPermissionUtil(): PlayPermissionUtil {
        return permissionUtil
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