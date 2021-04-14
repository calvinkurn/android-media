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
        private val livePusherWrapper: ApsaraLivePusherWrapper
) {

    class Factory @Inject constructor() {
        fun create(
                livePusherWrapper: ApsaraLivePusherWrapper
        ): PlayLiveStateProcessor {
            return PlayLiveStateProcessor(
                    livePusherWrapper = livePusherWrapper
            )
        }
    }

    private val mListeners = mutableListOf<PlayLiveStateListener>()
    private var mLiveState: PlayLivePusherState? = null

    private val localStorage: SharedPreferences
        get() = livePusherWrapper.context.getSharedPreferences(PlayCountDownTimer.PLAY_BROADCAST_PREFERENCE, Context.MODE_PRIVATE)

    private val localStorageEditor: SharedPreferences.Editor
        get() = localStorage.edit()

    private var mPauseDuration: Long? = null
    private var isLiveStarted: Boolean = false

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

    fun onResume() {
        if (livePusherWrapper.pusherState is ApsaraLivePusherState.Stop) {
            livePusherWrapper.destroy()
        } else if (!isLiveStarted) livePusherWrapper.resume()

        if (isLiveStarted && livePusherWrapper.pusherState is ApsaraLivePusherState.Pause
                && isReachMaximumPauseDuration()) {
            reachMaximumPauseDuration()
        } else shouldContinueLiveStreaming()
    }

    fun onPause() {
        livePusherWrapper.pause()
        setLastPauseMillis()
    }

    fun onDestroy() {
        if (livePusherWrapper.pusherState == ApsaraLivePusherState.Idle
                || livePusherWrapper.pusherState == ApsaraLivePusherState.Stop) {
            removeLastPauseMillis()
        }
        livePusherWrapper.destroy()
    }

    fun setPauseDuration(duration: Long) {
        this.mPauseDuration = duration
    }

    fun addStateListener(listener: PlayLiveStateListener) {
        mListeners.add(listener)
    }

    fun removeStateListener(listener: PlayLiveStateListener) {
        mListeners.remove(listener)
    }

    private fun reachMaximumPauseDuration() {
        broadcastState(PlayLivePusherState.Stop(isStopped = false, shouldNavigate = true))
    }

    private fun shouldContinueLiveStreaming() {
        broadcastState(PlayLivePusherState.Resume(isResumed = false))
    }

    private fun handleState(state: ApsaraLivePusherState) {
        when(state) {
            ApsaraLivePusherState.Connecting -> broadcastState(PlayLivePusherState.Connecting)
            ApsaraLivePusherState.Start -> {
                broadcastState(PlayLivePusherState.Start)
                isLiveStarted = true
            }
            ApsaraLivePusherState.Resume -> {
                if (isLiveStarted) {
                    if (mLiveState is PlayLivePusherState.Error) { // will fix this later
                        broadcastState(PlayLivePusherState.Recovered)
                    } else broadcastState(PlayLivePusherState.Resume(isResumed = true))
                }
            }
            ApsaraLivePusherState.Pause -> broadcastState(PlayLivePusherState.Pause)
            ApsaraLivePusherState.Restart -> broadcastState(PlayLivePusherState.Resume(isResumed = true))
            ApsaraLivePusherState.Recovered -> broadcastState(PlayLivePusherState.Recovered)
            ApsaraLivePusherState.Stop -> broadcastState(PlayLivePusherState.Stop(isStopped = true, shouldNavigate = false))
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
        mLiveState = state
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

    private fun removeLastPauseMillis() {
        localStorageEditor.remove(KEY_PAUSE_TIME)?.apply()
    }

    companion object {

        const val KEY_PAUSE_TIME = "play_broadcast_pause_time"
    }
}
