package com.tokopedia.play.broadcaster.view.viewmodel

import android.Manifest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.play.broadcaster.dispatcher.PlayBroadcastDispatcher
import com.tokopedia.play.broadcaster.mocker.PlayBroadcastMocker
import com.tokopedia.play.broadcaster.pusher.PlayPusher
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherInfoState
import com.tokopedia.play.broadcaster.pusher.state.PlayPusherNetworkState
import com.tokopedia.play.broadcaster.ui.model.*
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionState
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionUtil
import com.tokopedia.play.broadcaster.view.event.ScreenStateEvent
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.Event
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named


/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastViewModel  @Inject constructor(
        private val playPusher: PlayPusher,
        private val permissionUtil: PlayPermissionUtil,
        @Named(PlayBroadcastDispatcher.MAIN) private val mainDispatcher: CoroutineDispatcher,
        @Named(PlayBroadcastDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + mainDispatcher)

    val observableChannelInfo: LiveData<ChannelInfoUiModel>
        get() = _observableChannelInfo
    val observableTotalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val observableTotalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableScreenStateEvent: LiveData<Event<ScreenStateEvent>>
        get() = _observableScreenStateEvent
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

    val channelInfo: ChannelInfoUiModel?
        get() = _observableChannelInfo.value

    private val _observableChannelInfo = MutableLiveData<ChannelInfoUiModel>()
    private val _observableTotalView = MutableLiveData<TotalViewUiModel>()
    private val _observableTotalLike = MutableLiveData<TotalLikeUiModel>()
    private val _observableScreenStateEvent = MutableLiveData<Event<ScreenStateEvent>>()
    private val _observableChatList = MutableLiveData<MutableList<PlayChatUiModel>>()
    private val _observableNewMetric = MutableLiveData<Event<PlayMetricUiModel>>()
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
    private val _observableLiveInfoState = MediatorLiveData<Event<PlayPusherInfoState>>().apply {
        addSource(playPusher.getObservablePlayPusherInfoState()) {
            value = Event(it)
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
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    fun getConfiguration() {
        _observableScreenStateEvent.value = Event(ScreenStateEvent.ShowLoading)
        scope.launch {
            val config = PlayBroadcastMocker.getMockConfiguration()
            if (config.isUserWhitelisted) {
                playPusher.addMaxStreamDuration(config.maxLiveStreamDuration)
                if (config.isHaveOnGoingLive) {
                    _observableScreenStateEvent.value = Event(ScreenStateEvent.ShowUserInteractionPage(config.channelId))
                } else {
                    _observableScreenStateEvent.value = Event(ScreenStateEvent.ShowSetupPage)
                }
            } else {
                // TODO("ask PO for this case")
                _observableScreenStateEvent.value = Event(ScreenStateEvent.ShowDialogError("Warning!", "Not a whitelisted user"))
            }
        }
    }

    fun getChannel(channelId: String): ChannelInfoUiModel {
        val channelInfo = PlayBroadcastMocker.getMockActiveChannel()
        _observableChannelInfo.value = channelInfo
        _observableTotalView.value = PlayBroadcastMocker.getMockTotalView()
        _observableTotalLike.value = PlayBroadcastMocker.getMockTotalLike()
        startWebSocket(
                channelInfo
        )
        return channelInfo
    }

    private fun updateChannelStatus(status: PlayChannelStatus) {
        // TODO("update channel status")
    }

    fun startLiveStreaming() {
        _observableScreenStateEvent.value = Event(ScreenStateEvent.ShowCountDown)
    }

    fun startPushBroadcast(ingestUrl: String) {
        scope.launch {
            if (ingestUrl.isNotEmpty()) {
                playPusher.startPush(ingestUrl)
                updateChannelStatus(PlayChannelStatus.Active)
            }
        }
    }

    fun stopPushBroadcast() {
        scope.launch {
            playPusher.stopPush()
            playPusher.stopPreview()
            updateChannelStatus(PlayChannelStatus.InActive)
        }
    }

    fun getPlayPusher(): PlayPusher {
        return playPusher
    }

    fun getPermissionUtil(): PlayPermissionUtil {
        return permissionUtil
    }

    private fun startWebSocket(channelInfo: ChannelInfoUiModel) {
        scope.launch {
            // TODO("connect socket")
        }
    }

    private suspend fun onRetrievedNewChat(newChat: PlayChatUiModel) = withContext(mainDispatcher) {
        val currentChatList = _observableChatList.value ?: mutableListOf()
        currentChatList.add(newChat)
        _observableChatList.value = currentChatList
    }

    private suspend fun onRetrievedNewMetric(newMetric: PlayMetricUiModel) = withContext(mainDispatcher) {
        _observableNewMetric.value = Event(newMetric)
    }

    /**
     * mock
     */
    private fun mockChatList() {
        scope.launch(ioDispatcher) {
            while(isActive) {
                delay(1000)
                onRetrievedNewChat(
                    PlayBroadcastMocker.getMockChat()
                )
            }
        }
    }

    private fun mockMetrics() {
        scope.launch(ioDispatcher) {
            while(isActive) {
                delay(3000)
                onRetrievedNewMetric(
                    PlayBroadcastMocker.getMockMetric()
                )
            }
        }
    }
}