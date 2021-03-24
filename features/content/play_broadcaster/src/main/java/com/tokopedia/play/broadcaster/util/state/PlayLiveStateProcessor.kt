package com.tokopedia.play.broadcaster.util.state

import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState
import com.tokopedia.play.broadcaster.util.state.observer.PlayLiveStateObserver
import com.tokopedia.play.broadcaster.util.timer.PlayCountDownTimer
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherErrorState
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState
import javax.inject.Inject


/**
 * Created by mzennis on 16/03/21.
 */
class PlayLiveStateProcessor(
        private val lifecycleOwner: LifecycleOwner,
        private val livePusherWrapper: ApsaraLivePusherWrapper,
        private val countDownTimer: PlayCountDownTimer
): PlayLiveStateObserver.Listener {

    class Factory @Inject constructor(
            private val lifecycleOwner: LifecycleOwner
    ) {
        fun create(
                livePusherWrapper: ApsaraLivePusherWrapper,
                countDownTimer: PlayCountDownTimer
        ): PlayLiveStateProcessor {
            return PlayLiveStateProcessor(
                    lifecycleOwner = lifecycleOwner,
                    livePusherWrapper = livePusherWrapper,
                    countDownTimer = countDownTimer
            )
        }
    }

    private val mListeners = mutableListOf<PlayLiveStateListener>()
    private val liveStateObserver = PlayLiveStateObserver(livePusherWrapper, listener = this)

    init {
        livePusherWrapper.addListener(object : ApsaraLivePusherWrapper.Listener{
            override fun onStateChanged(state: ApsaraLivePusherState) {
                handleState(state)
            }

            override fun onError(code: Int, throwable: Throwable) {
                handleError(code, throwable)
            }
        })
        addLifecycleObserver()
    }

    fun setPauseDuration(duration: Long) {
        liveStateObserver.setPauseDuration(duration)
    }

    fun addStateListener(listener: PlayLiveStateListener) {
        mListeners.add(listener)
    }

    fun removeStateListener(listener: PlayLiveStateListener) {
        mListeners.remove(listener)
    }

    override fun onReachMaximumPauseDuration() {
        broadcastState(PlayLivePusherState.Stop(shouldNavigate = true))
    }

    override fun onShouldContinueLiveStreaming() {
        broadcastState(PlayLivePusherState.Resume(isResumed = false))
    }

    private fun handleState(state: ApsaraLivePusherState) {
        when(state) {
            ApsaraLivePusherState.Connecting -> broadcastState(PlayLivePusherState.Connecting)
            ApsaraLivePusherState.Start -> {
                broadcastState(PlayLivePusherState.Start)
            }
            ApsaraLivePusherState.Resume -> {
                broadcastState(PlayLivePusherState.Resume(isResumed = true))
                countDownTimer.resume()
            }
            ApsaraLivePusherState.Pause -> {
                broadcastState(PlayLivePusherState.Pause)
                countDownTimer.pause()
            }
            ApsaraLivePusherState.Restart -> broadcastState(PlayLivePusherState.Resume(isResumed = true))
            ApsaraLivePusherState.Recovered -> broadcastState(PlayLivePusherState.Resume(isResumed = true))
            ApsaraLivePusherState.Stop -> removeLifecycleObserver()
            else -> {}
        }
    }

    private fun handleError(code: Int, throwable: Throwable) {
        broadcastState(
                PlayLivePusherState.Error(when (code) {
                    ApsaraLivePusherWrapper.PLAY_PUSHER_ERROR_SYSTEM_ERROR -> PlayLivePusherErrorState.SystemError
                    ApsaraLivePusherWrapper.PLAY_PUSHER_ERROR_NETWORK_POOR -> PlayLivePusherErrorState.NetworkPoor
                    ApsaraLivePusherWrapper.PLAY_PUSHER_ERROR_NETWORK_LOSS -> {
                        livePusherWrapper.reconnect()
                        PlayLivePusherErrorState.NetworkLoss
                    }
                    else -> PlayLivePusherErrorState.ConnectFailed()
                }, throwable)
        )
    }

    private fun broadcastState(state: PlayLivePusherState) {
        mListeners.forEach { it.onStateChanged(state) }
    }

    private fun addLifecycleObserver() {
        lifecycleOwner.lifecycle.addObserver(liveStateObserver)
    }

    private fun removeLifecycleObserver() {
        lifecycleOwner.lifecycle.removeObserver(liveStateObserver)
    }
}