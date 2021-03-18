package com.tokopedia.play.broadcaster.util.state

import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState
import com.tokopedia.play.broadcaster.util.timer.PlayCountDownTimer
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherErrorState
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState
import com.tokopedia.play_common.util.ExoPlaybackExceptionParser
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import javax.inject.Inject


/**
 * Created by mzennis on 16/03/21.
 */
class PlayLiveStateProcessor(
        livePusherWrapper: ApsaraLivePusherWrapper,
        private val countDownTimer: PlayCountDownTimer
) {

    class Factory @Inject constructor() {
        fun create(
                livePusherWrapper: ApsaraLivePusherWrapper,
                countDownTimer: PlayCountDownTimer
        ): PlayLiveStateProcessor {
            return PlayLiveStateProcessor(livePusherWrapper, countDownTimer)
        }
    }

    private val mListeners = mutableListOf<PlayLiveStateListener>()

    private var mLivePusherState: PlayLivePusherState = PlayLivePusherState.Stopped

    init {
        livePusherWrapper.addListener(object : ApsaraLivePusherWrapper.Listener{
            override fun onStateChanged(state: ApsaraLivePusherState) {
                handleState(state)
            }

            override fun onError(code: Int, throwable: Throwable) {
                handleError(code, throwable)
            }
        })
    }

    fun addStateListener(listener: PlayLiveStateListener) {
        mListeners.add(listener)
    }

    fun removeStateListener(listener: PlayLiveStateListener) {
        mListeners.remove(listener)
    }

    private fun handleState(state: ApsaraLivePusherState) {
        when(state) {
            ApsaraLivePusherState.Connecting -> broadcastState(PlayLivePusherState.Connecting)
            ApsaraLivePusherState.Start -> {
                broadcastState(PlayLivePusherState.Started)
                mLivePusherState = PlayLivePusherState.Started
            }
            ApsaraLivePusherState.Resume -> {
                if (mLivePusherState !is PlayLivePusherState.Paused) return
                broadcastState(PlayLivePusherState.Resumed)
                mLivePusherState = PlayLivePusherState.Resumed
                countDownTimer.resume()
            }
            ApsaraLivePusherState.Pause -> {
                if (mLivePusherState !is PlayLivePusherState.Started
                        && mLivePusherState !is PlayLivePusherState.Resumed) return
                broadcastState(PlayLivePusherState.Paused)
                mLivePusherState = PlayLivePusherState.Paused
                countDownTimer.pause()
            }
            ApsaraLivePusherState.Restart -> broadcastState(PlayLivePusherState.Resumed)
            ApsaraLivePusherState.Recovered -> broadcastState(PlayLivePusherState.Resumed)
            else -> broadcastState(PlayLivePusherState.Stopped)
        }
    }

    private fun handleError(code: Int, throwable: Throwable) {
        broadcastState(
                PlayLivePusherState.Error(when (code) {
                    ApsaraLivePusherWrapper.PLAY_PUSHER_ERROR_SYSTEM_ERROR -> PlayLivePusherErrorState.SystemError
                    ApsaraLivePusherWrapper.PLAY_PUSHER_ERROR_NETWORK_POOR -> PlayLivePusherErrorState.NetworkPoor
                    ApsaraLivePusherWrapper.PLAY_PUSHER_ERROR_NETWORK_LOSS -> PlayLivePusherErrorState.NetworkLoss
                    else -> PlayLivePusherErrorState.ConnectFailed()
                }, throwable)
        )
    }

    private fun broadcastState(state: PlayLivePusherState) {
        mListeners.forEach { it.onStateChanged(state) }
    }

}