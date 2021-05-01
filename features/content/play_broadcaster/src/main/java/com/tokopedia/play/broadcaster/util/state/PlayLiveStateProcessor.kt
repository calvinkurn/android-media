package com.tokopedia.play.broadcaster.util.state

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState
import com.tokopedia.play.broadcaster.util.timer.PlayCountDownTimer
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherErrorState
import com.tokopedia.play.broadcaster.view.state.PlayLivePusherState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by mzennis on 16/03/21.
 */
class PlayLiveStateProcessor(
        private val livePusherWrapper: ApsaraLivePusherWrapper,
        private val scope: CoroutineScope
) {

    class Factory @Inject constructor() {
        fun create(
                livePusherWrapper: ApsaraLivePusherWrapper,
                scope: CoroutineScope
        ): PlayLiveStateProcessor {
            return PlayLiveStateProcessor(
                    livePusherWrapper = livePusherWrapper,
                    scope = scope
            )
        }
    }

    private val mListeners = mutableListOf<PlayLiveStateListener>()

    private var autoReconnectJob: Job? = null

    private val localStorage: SharedPreferences
        get() = livePusherWrapper.context.getSharedPreferences(PlayCountDownTimer.PLAY_BROADCAST_PREFERENCE, Context.MODE_PRIVATE)

    private val localStorageEditor: SharedPreferences.Editor
        get() = localStorage.edit()

    private var mPauseDuration: Long? = null
    private var isLiveStarted: Boolean = false

    private val apsaraListener = object : ApsaraLivePusherWrapper.Listener{
        override fun onStateChanged(state: ApsaraLivePusherState) {
            handleState(state)
        }

        override fun onError(code: Int, throwable: Throwable) {
            if (livePusherWrapper.pusherState !is ApsaraLivePusherState.Pause
                    && livePusherWrapper.pusherState !is ApsaraLivePusherState.Stop)
            handleError(code, throwable)
        }
    }

    init {
        livePusherWrapper.addListener(apsaraListener)
    }

    fun onResume() {
        if (livePusherWrapper.pusherState is ApsaraLivePusherState.Stop) {
            livePusherWrapper.destroy()
        } else if (!isLiveStarted) {
            scope.launch {
                livePusherWrapper.resume()
            }
        }

        if (isLiveStarted) {
            if (isReachMaximumPauseDuration()) reachMaximumPauseDuration()
            else {
                scope.launch {
                    livePusherWrapper.resume()
                    if (!livePusherWrapper.isActivePushing) {
                        livePusherWrapper.reconnect()
                        broadcastState(PlayLivePusherState.Error(PlayLivePusherErrorState.NetworkLoss, IllegalStateException("Connection Loss")))
                    }
                }
            }
        } else shouldContinueLiveStreaming()
    }

    fun onPause() {
        cancelJob()
        livePusherWrapper.pause()
        setLastPauseMillis()
    }

    fun onDestroy() {
        if (livePusherWrapper.pusherState == ApsaraLivePusherState.Idle
                || livePusherWrapper.pusherState == ApsaraLivePusherState.Stop) {
            removeLastPauseMillis()
        }
        livePusherWrapper.removeListener(apsaraListener)
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
                    broadcastState(PlayLivePusherState.Resume(isResumed = true))
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
                    ApsaraLivePusherWrapper.PLAY_PUSHER_ERROR_NETWORK_LOSS,
                    ApsaraLivePusherWrapper.PLAY_PUSHER_ERROR_RECONNECTION_FAILED -> {
                        autoReconnect()
                        PlayLivePusherErrorState.NetworkLoss
                    }
                    else -> PlayLivePusherErrorState.ConnectFailed()
                }, throwable)
        )
    }

    private fun autoReconnect() {
        autoReconnectJob = scope.launch {
            livePusherWrapper.reconnect()
        }
    }

    private fun cancelJob() {
        autoReconnectJob?.cancel()
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

    private fun removeLastPauseMillis() {
        localStorageEditor.remove(KEY_PAUSE_TIME)?.apply()
    }

    companion object {

        const val KEY_PAUSE_TIME = "play_broadcast_pause_time"
    }
}
