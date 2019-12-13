package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.*
import com.tokopedia.play.data.mapper.PlaySocketMapper
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.GetChannelInfoUseCase
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.type.PlayVODType
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
        private val playSocket: PlaySocket,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers) {

    val observableVideoState: LiveData<TokopediaPlayVideoState>
        get() = playManager.getObservablePlayVideoState()

    val observableVOD: LiveData<PlayVODType>
        get() = _observableVOD
    private val _observableVOD by lazy {
        MutableLiveData<PlayVODType>()
    }

    private val _observableGetChannelInfo = MutableLiveData<Result<Channel>>()
    val observeGetChannelInfo: LiveData<Result<Channel>> = _observableGetChannelInfo

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
            val response = withContext(Dispatchers.IO) {
                getChannelInfoUseCase.channelId = channelId
                getChannelInfoUseCase.executeOnBackground()
            }
            _observableGetChannelInfo.value = Success(response)
        }) {
            _observableGetChannelInfo.value = Fail(it)
        }
    }

    fun startCurrentVideo() {
        playManager.resumeCurrentVideo()
    }

    fun startWebSocket(channelId: String, gcToken: String) {
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

    fun initVideo() {
//         startVideoWithUrlString("http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4",  false)
        startVideoWithUrlString("rtmp://fms.105.net/live/rmc1", true)
    }

    private fun startVideoWithUrlString(urlString: String, isLive: Boolean) {
        playManager.safePlayVideoWithUriString(urlString, isLive)
        _observableVOD.value =
                if (isLive) PlayVODType.Live(playManager.videoPlayer)
                else PlayVODType.Replay(playManager.videoPlayer)
    }

    // TODO don't forget to destroy socket
    fun destroy() {
        playSocket.destroy()
    }
}