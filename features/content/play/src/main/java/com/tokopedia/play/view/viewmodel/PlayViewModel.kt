package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.Channel
import com.tokopedia.play.domain.GetChannelInfoUseCase
import com.tokopedia.play.view.type.PlayVODType
import com.tokopedia.play_common.player.TokopediaPlayManager
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.WebSocket
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayViewModel @Inject constructor(
        private val playManager: TokopediaPlayManager,
        private val getChannelInfoUseCase: GetChannelInfoUseCase,
        private val userSessionInterface: UserSessionInterface,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers) {

    val observableVOD: LiveData<PlayVODType>
        get() = _observableVOD

    val observableVideoState: LiveData<TokopediaPlayVideoState>
        get() = playManager.getObservablePlayVideoState()

    val observeChannel: LiveData<Result<Channel>>
        get() = _channelInfoResult

    private val _observableVOD by lazy {
        MutableLiveData<PlayVODType>()
    }

    private val _channelInfoResult by lazy {
        MutableLiveData<Result<Channel>>()
    }

    fun getChannelInfo(channelId: String) {
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                getChannelInfoUseCase.channelId = channelId
                getChannelInfoUseCase.executeOnBackground()
            }
            _channelInfoResult.value = Success(response)
        }) {
            _channelInfoResult.value = Fail(it)
        }
    }

    fun startCurrentVideo() {
        playManager.resumeCurrentVideo()
    }

    fun startWebsocket(url: String) {
        val websocketSubscriber = object : WebSocketSubscriber() {

            override fun onOpen(webSocket: WebSocket) {

            }

            override fun onClose() {

            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {
                // TODO create mapper => PlayPresenter.kt
            }

            override fun onError(e: Throwable) {

            }
        }

        val websocket = RxWebSocket[url, userSessionInterface.accessToken]
        websocket?.subscribe(websocketSubscriber)
    }

    fun initVideo() {
//        startVideoWithUrlString("http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4", false)
        startVideoWithUrlString("rtmp://fms.105.net/live/rmc1", true)
    }

    private fun startVideoWithUrlString(urlString: String, isLive: Boolean) {
        playManager.safePlayVideoWithUriString(urlString, isLive)
        _observableVOD.value =
                if (isLive) PlayVODType.Live(playManager.videoPlayer)
                else PlayVODType.Replay(playManager.videoPlayer)
    }
}