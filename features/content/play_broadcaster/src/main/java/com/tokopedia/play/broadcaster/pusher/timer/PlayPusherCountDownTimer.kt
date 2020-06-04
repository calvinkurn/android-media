package com.tokopedia.play.broadcaster.pusher.timer

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.preference.PreferenceManager
import kotlin.math.max


/**
 * Created by mzennis on 25/05/20.
 */
class PlayPusherCountDownTimer(val context: Context,
                               private val maxLiveStreamDuration: Long = DEFAULT_MAX_LIVE_STREAM_DURATION) {

    private var countDownTimer: CountDownTimer? = null

    private var lastMillis: Long = maxLiveStreamDuration

    private var callback: PlayPusherCountDownTimerListener? = null
    private var sharedPreferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

    fun addCallback(callback: PlayPusherCountDownTimerListener) {
        this.callback = callback
    }

    fun start() {
        val lastSavedMillis = sharedPreferences?.getLong(
                PLAY_TIMER_LAST_STATE, maxLiveStreamDuration)
                ?:maxLiveStreamDuration
        countDownTimer = getCountDownTimer(lastSavedMillis)
        countDownTimer?.start()
    }

    fun stop() {
        countDownTimer?.cancel()
        clearLastState()
        destroy()
    }

    fun pause() {
        countDownTimer?.cancel()
        saveLastState()
        destroy()
    }

    private fun destroy() {
        countDownTimer = null
        sharedPreferences = null
        lastMillis = 0L
    }

    private fun saveLastState() {
        if (lastMillis > 0) {
            val editor = sharedPreferences?.edit()
            editor?.putLong(PLAY_TIMER_LAST_STATE, lastMillis)
            editor?.apply()
        }
    }

    private fun clearLastState() {
        val editor = sharedPreferences?.edit()
        editor?.remove(PLAY_TIMER_LAST_STATE)
        editor?.apply()
    }

    private fun getCountDownTimer(maxLiveStreamDuration: Long): CountDownTimer {
        return object : CountDownTimer(maxLiveStreamDuration, DEFAULT_COUNT_DOWN_INTERVAL) {
            override fun onFinish() {
                stop()
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
        const val PLAY_TIMER_LAST_STATE = "play_broadcast_timer_last_state"
    }
}