package com.tokopedia.play.broadcaster.pusher

import android.content.Context
import android.os.CountDownTimer
import com.tokopedia.abstraction.common.utils.LocalCacheHandler


/**
 * Created by mzennis on 25/05/20.
 */
class PlayPusherCountDownTimer(val context: Context,
                               private val maxLiveStreamDuration: Long = DEFAULT_MAX_LIVE_STREAM_DURATION) {

    private var countDownTimer: CountDownTimer? = null
    private var lastMillis: Long = maxLiveStreamDuration
    private var callback: PlayPusherCountDownTimerCallback? = null
    private var localCacheHandler: LocalCacheHandler? = null

    init {
        localCacheHandler = LocalCacheHandler(context, PLAY_TIMER_PREFERENCES)
    }

    fun addCallback(callback: PlayPusherCountDownTimerCallback) {
        this.callback = callback
    }

    fun start() {
        val lastSavedMillis = localCacheHandler?.getLong(PLAY_TIMER_LAST_STATE,
                maxLiveStreamDuration)?: maxLiveStreamDuration
        countDownTimer = getCountDownTimer(lastSavedMillis)
        countDownTimer?.start()
    }

    fun stop() {
        countDownTimer?.cancel()
        saveLastState()
        destroy()
    }

    private fun destroy() {
        countDownTimer = null
        callback = null
        localCacheHandler = null
        lastMillis = 0L
    }

    private fun saveLastState() {
        localCacheHandler?.putLong(PLAY_TIMER_LAST_STATE, lastMillis)
    }

    private fun getCountDownTimer(maxLiveStreamDuration: Long): CountDownTimer {
        return object : CountDownTimer(maxLiveStreamDuration, DEFAULT_COUNT_DOWN_INTERVAL) {
            override fun onFinish() {
                callback?.onCountDownFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                lastMillis = millisUntilFinished
                callback?.onCountDownActive(millisUntilFinished)
            }
        }
    }

    companion object {
        const val DEFAULT_MAX_LIVE_STREAM_DURATION = 1800000L
        const val DEFAULT_COUNT_DOWN_INTERVAL = 1000L
        const val PLAY_TIMER_PREFERENCES = "play_broadcast_timer"
        const val PLAY_TIMER_LAST_STATE = "play_broadcast_timer_last_state"
    }
}