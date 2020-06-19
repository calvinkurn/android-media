package com.tokopedia.play.broadcaster.view.viewmodel

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.domain.usecase.CreateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
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
class PlayBroadcastViewModel  @Inject constructor(
        private val playPusher: PlayPusher,
        private val permissionUtil: PlayPermissionUtil,
        private val getConfigurationUseCase: GetConfigurationUseCase,
        private val createChannelUseCase: CreateChannelUseCase,
        private val dispatcher: CoroutineDispatcherProvider,
        private val userSession: UserSessionInterface
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val channelId: String
        get() = observableConfigInfo.value?.draftChannelId?.toString() ?: throw IllegalStateException("Channel ID has not been retrieved")

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
    private val _observableLiveInfoState = MediatorLiveData<PlayPusherInfoState>().apply {
        addSource(playPusher.getObservablePlayPusherInfoState()) {
            value = it
        }
    }
    private val _observablePermissionState = MediatorLiveData<PlayPermissionState>().apply {
        addSource(permissionUtil.getObservablePlayPermissionState()) {
            value = it
        }
    }

    init {
        permissionUtil.checkPermission(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO))
        playPusher.create()

        mockChatList()
        mockMetrics()
        mockProductList()
        mockShareData()
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    fun getConfiguration() {
        scope.launch {
            val configurationUiModel = PlayBroadcastMocker.getMockConfiguration()
            _observableConfigInfo.value = configurationUiModel

            // TODO("tidying later, still waiting for the API to finish")
            if (configurationUiModel.haveOnGoingLive) {
                getChannel(configurationUiModel.activeChannelId)
            }

            // TODO("match local countdown timer with socket")
            playPusher.addMaxStreamDuration(configurationUiModel.durationConfig.duration)
            playPusher.addMaxPauseDuration(configurationUiModel.durationConfig.pauseDuration)
        }
    }

    // TODO("tidying later, still waiting for the API to finish")
    fun startPrepareChannel() {
        scope.launch {
            if (configuration?.streamAllowed == true
                    && configuration?.haveOnGoingLive == false
                    && configuration?.draftChannelId == 0) {
                createChannel()
            }
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

    fun startPushBroadcast(ingestUrl: String) {
        scope.launch {
            if (ingestUrl.isNotEmpty()) {
                startWebSocket()
                updateChannelStatus(PlayChannelStatus.Active)
                playPusher.startPush(ingestUrl)
            }
        }
    }

    fun stopPushBroadcast() {
        scope.launch {
            updateChannelStatus(PlayChannelStatus.Finish)
            playPusher.stopPush()
            playPusher.stopPreview()
        }
    }

    private fun startWebSocket() {
        // TODO("connect socket")
        // TODO("retrieve & update count down")
    }

    private suspend fun onRetrievedNewChat(newChat: PlayChatUiModel) = withContext(dispatcher.main) {
        val currentChatList = _observableChatList.value ?: mutableListOf()
        currentChatList.add(newChat)
        _observableChatList.value = currentChatList
    }

    private suspend fun onRetrievedNewMetric(newMetric: PlayMetricUiModel) = withContext(dispatcher.main) {
        _observableNewMetric.value = Event(newMetric)
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