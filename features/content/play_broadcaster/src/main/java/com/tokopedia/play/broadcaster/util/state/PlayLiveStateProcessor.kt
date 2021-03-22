package com.tokopedia.play.broadcaster.util.state

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState
import com.tokopedia.play.broadcaster.util.timer.PlayCountDownTimer
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherErrorState
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState
import javax.inject.Inject


/**
 * Created by mzennis on 16/03/21.
 */
class PlayLiveStateProcessor(
        private val livePusherWrapper: ApsaraLivePusherWrapper,
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

    private var mPauseDuration: Long? = null

    private val localStorage: SharedPreferences
        get() = livePusherWrapper.context.getSharedPreferences(PlayCountDownTimer.PLAY_BROADCAST_PREFERENCE, Context.MODE_PRIVATE)

    private val localStorageEditor: SharedPreferences.Editor
        get() = localStorage.edit()

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

    fun setPauseDuration(duration: Long) {
        mPauseDuration = duration
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
            }
            ApsaraLivePusherState.Resume -> {
                if (isReachMaximumPauseDuration()) {
                    broadcastState(PlayLivePusherState.Stopped(shouldNavigate = true))
                } else {
                    broadcastState(PlayLivePusherState.Resumed)
                    countDownTimer.resume()
                }
            }
            ApsaraLivePusherState.Pause -> {
                broadcastState(PlayLivePusherState.Paused)
                countDownTimer.pause()
                setLastPauseMillis()
            }
            ApsaraLivePusherState.Restart -> broadcastState(PlayLivePusherState.Resumed)
            ApsaraLivePusherState.Recovered -> broadcastState(PlayLivePusherState.Resumed)
            else -> removeLastPauseMillis()
        }
    }

    private fun removeLastPauseMillis() {
        localStorageEditor.remove(KEY_PAUSE_TIME)?.apply()
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

    private fun setLastPauseMillis() {
        localStorageEditor.putLong(KEY_PAUSE_TIME, System.currentTimeMillis())?.apply()
    }

    private fun isReachMaximumPauseDuration(): Boolean {
        val maxPauseMillis = mPauseDuration
        if (maxPauseMillis != null) {
            val lastPauseMillis = localStorage.getLong(KEY_PAUSE_TIME, 0L)
            if (lastPauseMillis > 0 && reachMaximumPauseDuration(lastPauseMillis, maxPauseMillis)) {
                localStorageEditor.remove(KEY_PAUSE_TIME)?.apply()
                return true
            }
        }
        return false
    }

    private fun reachMaximumPauseDuration(lastMillis: Long, maxPauseMillis: Long): Boolean {
        val currentMillis = System.currentTimeMillis()
        return ((currentMillis - lastMillis) > maxPauseMillis)
    }

    companion object {

        const val KEY_PAUSE_TIME = "play_broadcast_pause_time"
    }
}