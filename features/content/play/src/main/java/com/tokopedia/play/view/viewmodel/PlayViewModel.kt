package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.GetChannelInfoUseCase
import com.tokopedia.play.domain.GetVideoStreamUseCase
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.type.PlayVideoType
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play_common.player.TokopediaPlayManager
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayViewModel @Inject constructor(
        private val playManager: TokopediaPlayManager,
        private val getChannelInfoUseCase: GetChannelInfoUseCase,
        private val getVideoStreamUseCase: GetVideoStreamUseCase,
        private val playSocket: PlaySocket,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers) {

    private val _observableVideoType = MutableLiveData<PlayVideoType>()

    val observableVideoProperty: LiveData<VideoPropertyUiModel> = MediatorLiveData<VideoPropertyUiModel>().apply {
        addSource(_observableVideoType) { value = VideoPropertyUiModel(it, value?.state ?: TokopediaPlayVideoState.NotConfigured) }
        addSource(playManager.getObservablePlayVideoState()) { value = VideoPropertyUiModel(value?.type ?: PlayVideoType.Live, it) }
    }

    val observableVOD: LiveData<ExoPlayer>
        get() = _observableVOD
    private val _observableVOD = MutableLiveData<ExoPlayer>()

    private val _observableGetChannelInfo = MutableLiveData<Result<ChannelInfoUiModel>>()
    val observeGetChannelInfo: LiveData<Result<ChannelInfoUiModel>> = _observableGetChannelInfo

    private val _observableChatListSocket = MutableLiveData<PlayChat>()
    val observableChatListSocket: LiveData<PlayChat> = _observableChatListSocket

    private val _observableTotalViewsSocket = MutableLiveData<TotalView>()
    val observableTotalViewsSocket: LiveData<TotalView> = _observableTotalViewsSocket

    private val _observablePinnedMessageSocket = MutableLiveData<PinnedMessage>()
    val observablePinnedMessageSocket: LiveData<PinnedMessage> = _observablePinnedMessageSocket

    private val _observableQuickReplySocket = MutableLiveData<QuickReply>()
    val observableQuickReplySocket: LiveData<QuickReply> = _observableQuickReplySocket

    private val _observableBannedFreezeSocket = MutableLiveData<BannedFreeze>()
    val observableBannedFreezeSocket: LiveData<BannedFreeze> = _observableBannedFreezeSocket

    fun getChannelInfo(channelId: String) {
        launchCatchError(block = {
            val (channel, videoStream) = withContext(Dispatchers.IO) {
                getChannelInfoUseCase.channelId = channelId
                val channel = getChannelInfoUseCase.executeOnBackground()

                getVideoStreamUseCase.channelId = channelId
                val videoStream = getVideoStreamUseCase.executeOnBackground()

                return@withContext Pair(channel, videoStream)
            }
            /**
             * If Live => start web socket
             */
            _observableVideoType.value = if (videoStream.isLive) PlayVideoType.Live else PlayVideoType.VOD
            if (videoStream.isLive) startWebSocket(channelId, channel.gcToken)
            playVideoStream(videoStream)

            _observableGetChannelInfo.value = Success(
                    createChannelInfoUiModel(channel, videoStream)
            )
        }) {
            _observableGetChannelInfo.value = Fail(it)
        }
    }

    fun startCurrentVideo() {
        playManager.resumeCurrentVideo()
    }

//    fun initVideo() {
//         startVideoWithUrlString("http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4",  false)
////        startVideoWithUrlString("rtmp://fms.105.net/live/rmc1", true)
//    }

    // TODO don't forget to destroy socket
    fun destroy() {
        playSocket.destroy()
    }

    private fun startWebSocket(channelId: String, gcToken: String) {
        playSocket.channelId = channelId
        playSocket.gcToken = gcToken
        playSocket.connect( onOpen ={
            // Todo, handle on open web socket
        }, onClose =  {
            // Todo, handle on close web socket
        }, onMessageReceived =  { response ->
            val socketMapper = PlaySocketMapper(response)
            when (val result = socketMapper.mapping()) {
                is TotalLike -> {

                }
                is TotalView -> {
                    _observableTotalViewsSocket.value = result
                }
                is PlayChat -> {
                    _observableChatListSocket.value = result
                }
                is PinnedMessage -> {
                    _observablePinnedMessageSocket.value = result
                }
                is QuickReply -> {
                    _observableQuickReplySocket.value = result
                }
                is BannedFreeze -> {
                    _observableBannedFreezeSocket.value = result
                }
            }
        }, onError = {
            // Todo, handle on error web socket
        })
    }

    private fun startVideoWithUrlString(urlString: String, isLive: Boolean) {
        playManager.safePlayVideoWithUriString(urlString, isLive)
        _observableVOD.value = playManager.videoPlayer
    }

    private fun playVideoStream(videoStream: VideoStream) {
        if (videoStream.isActive) {
            startVideoWithUrlString(videoStream.androidStreamHd, videoStream.isLive)
        }
    }

    private fun createChannelInfoUiModel(channel: Channel, videoStream: VideoStream) = ChannelInfoUiModel(
            channelId = channel.channelId,
            title = channel.title,
            description = channel.description,
            videoType = if (videoStream.isLive) PlayVideoType.Live else PlayVideoType.VOD,
            partner = PartnerUiModel(
                    PartnerType.getTypeByValue(channel.partnerType),
                    channel.partnerId
            ),
            isActive = videoStream.isActive,
            pinnedMessage = if (channel.pinnedMessage.pinnedMessageId <= 0) null else PinnedMessageUiModel(
                    channel.pinnedMessage.redirectUrl,
                    channel.pinnedMessage.title,
                    channel.pinnedMessage.message
            ),
            quickReply = channel.quickReply,
            totalView = channel.totalViews
    )
}