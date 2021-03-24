package com.tokopedia.play.broadcaster.util.state.observer

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.broadcaster.pusher.ApsaraLivePusherWrapper
import com.tokopedia.play.broadcaster.pusher.state.ApsaraLivePusherState
import com.tokopedia.play.broadcaster.util.timer.PlayCountDownTimer


/**
 * Created by mzennis on 24/03/21.
 */
class PlayLiveStateObserver(
        private val livePusherWrapper: ApsaraLivePusherWrapper,
        private val listener: Listener
) : LifecycleObserver {
    
    private val localStorage: SharedPreferences
        get() = livePusherWrapper.context.getSharedPreferences(PlayCountDownTimer.PLAY_BROADCAST_PREFERENCE, Context.MODE_PRIVATE)

    private val localStorageEditor: SharedPreferences.Editor
        get() = localStorage.edit()
    
    private var mPauseDuration: Long? = null
    
    fun setPauseDuration(pauseDuration: Long) {
        this.mPauseDuration = pauseDuration
    }
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (livePusherWrapper.pusherState == ApsaraLivePusherState.Pause
                && isReachMaximumPauseDuration()) {
            listener.onReachMaximumPauseDuration()
        } else {
            listener.onShouldContinueLiveStreaming()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        setLastPauseMillis()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onDestroy() {
        if (livePusherWrapper.pusherState == ApsaraLivePusherState.Idle
                || livePusherWrapper.pusherState == ApsaraLivePusherState.Stop) {
            removeLastPauseMillis()
        }
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

    interface Listener {

        fun onReachMaximumPauseDuration()
        fun onShouldContinueLiveStreaming()
    }

    companion object {

        const val KEY_PAUSE_TIME = "play_broadcast_pause_time"
    }
}