package com.tokopedia.play.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.Like
import com.tokopedia.play.data.View
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

    private val _observableChatList = MutableLiveData<PlayChat>()
    val observableChatList: LiveData<PlayChat> = _observableChatList

    private val _observableTotalViewsSocket = MutableLiveData<View>()
    val observableTotalViewsSocket: LiveData<View> = _observableTotalViewsSocket

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
        playManager.startCurrentVideo()
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
                is View -> {
                    _observableTotalViewsSocket.value = result
                }

            }
        }, onError = {
            // Todo, handle on error web socket
        })
    }

    fun initVideo() {
        // startVideoWithUrlString("http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4",  false)
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


//    private val listOfUser = listOf(
//            "Rifqi",
//            "Meyta",
//            "IJ",
//            "Yehez"
//    )
//
//    private val listOfMessage = listOf(
//            "Great product!",
//            "I watched all of that till te end and i decided i will buy this dress.",
//            "Great, wellspoken review. Such a wonderful information. Thanks a lot!"
//    )
//
//    fun startObservingChatList() {
//        launch {
//            var id = 0
//            while (job.isActive) {
//                _observableChatList.value =
//                        PlayChat(
//                                ++id,
//                                listOfUser.random(),
//                                listOfMessage.random()
//                        )
//                delay(5000)
//            }
//        }
//    }
}