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
import com.tokopedia.play.broadcaster.util.event.Event
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionState
import com.tokopedia.play.broadcaster.util.permission.PlayPermissionUtil
import com.tokopedia.play.broadcaster.view.event.ScreenStateEvent
import com.tokopedia.play.broadcaster.view.uimodel.ChannelInfoUiModel
import com.tokopedia.play.broadcaster.view.uimodel.PlayChannelStatus
import com.tokopedia.play.broadcaster.view.uimodel.TotalLikeUiModel
import com.tokopedia.play.broadcaster.view.uimodel.TotalViewUiModel
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Named


/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastViewModel  @Inject constructor(
        private val playPusher: PlayPusher,
        private val permissionUtil: PlayPermissionUtil,
        @Named(PlayBroadcastDispatcher.MAIN) private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher)

    val channelInfo: LiveData<ChannelInfoUiModel>
        get() = _observableChannelInfo
    val totalView: LiveData<TotalViewUiModel>
        get() = _observableTotalView
    val totalLike: LiveData<TotalLikeUiModel>
        get() = _observableTotalLike
    val observableScreenStateEvent: LiveData<Event<ScreenStateEvent>>
        get() = _observableScreenStateEvent
    val observableLiveInfoState: LiveData<Event<PlayPusherInfoState>>
        get() = _observableLiveInfoState
    val observableLiveNetworkState: LiveData<Event<PlayPusherNetworkState>>
        get() = _observableLiveNetworkState
    val observablePermissionState: LiveData<PlayPermissionState>
        get() = _observablePermissionState

    private val _observableChannelInfo = MutableLiveData<ChannelInfoUiModel>()
    private val _observableTotalView = MutableLiveData<TotalViewUiModel>()
    private val _observableTotalLike = MutableLiveData<TotalLikeUiModel>()
    private val _observableScreenStateEvent = MutableLiveData<Event<ScreenStateEvent>>()
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
    }

    fun getConfiguration() {
        _observableScreenStateEvent.value = Event(ScreenStateEvent.ShowLoading)
        scope.launch {
            val configuration = PlayBroadcastMocker.getMockConfiguration()
            if (configuration.isUserWhitelisted) {
                playPusher.addMaxStreamDuration(configuration.maxLiveStreamDuration)
                if (configuration.isHaveOnGoingLive) {
                    _observableScreenStateEvent.value = Event(ScreenStateEvent.ShowLivePage(configuration.channelId))
                } else {
                    _observableScreenStateEvent.value = Event(ScreenStateEvent.ShowPreparePage)
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

    private fun startWebSocket(channelInfo: ChannelInfoUiModel) {
        scope.launch {
            // TODO("connect socket")
        }
    }

    fun getPlayPusher(): PlayPusher {
        return playPusher
    }

    fun getPermissionUtil(): PlayPermissionUtil {
        return permissionUtil
    }
}