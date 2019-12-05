package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.Channel
import com.tokopedia.play.domain.GetChannelInfoUseCase
import com.tokopedia.play_common.player.TokopediaPlayManager
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

    val observableVODPlayer: LiveData<ExoPlayer>
        get() = _observableVODPlayer

    val observeChannel: LiveData<Result<Channel>>
        get() = _channelInfoResult

    private val _observableVODPlayer by lazy {
        MutableLiveData<ExoPlayer>()
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

    fun startWebsocket(url: String) {
        val websocketSubscriber = object : WebSocketSubscriber() {

            override fun onOpen(webSocket: WebSocket) {

            }

            override fun onClose() {

            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {

            }

            override fun onError(e: Throwable) {

            }
        }

        val websocket = RxWebSocket[url, userSessionInterface.accessToken]
        websocket?.subscribe(websocketSubscriber)
    }

    fun startVideoWithUrlString(urlString: String) {
        playManager.playVideoWithString(urlString)
        _observableVODPlayer.value = playManager.videoPlayer
    }
}