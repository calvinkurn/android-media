package com.tokopedia.play.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.GetChannelInfoUseCase
import com.tokopedia.play.view.type.PlayVODType
import com.tokopedia.play_common.player.TokopediaPlayManager
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    val observeGetChannelInfo: LiveData<Result<Channel>>
        get() = _observableGetChannelInfo
    private val _observableGetChannelInfo by lazy {
        MutableLiveData<Result<Channel>>()
    }

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

    fun startWebsocket(channelId: String, gcToken: String) {
        playSocket.channelId = channelId
        playSocket.gcToken = gcToken
        playSocket.connect(onOpen = {
            Log.wtf("Meyta", "socket open")
        }, onClose =  {
            Log.wtf("Meyta", "socket close")
        }, onMessageReceived =  { response ->
            // PlayWebsocketMapper > response
            Log.wtf("Meyta", "message: ${response.type}")
        }, onError = {

            Log.wtf("Meyta", "error")
        })
    }

    fun initVideo() {
        //startVideoWithUrlString("http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4",  false)
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