package com.tokopedia.play.broadcaster.pusher.timer

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import java.util.concurrent.TimeUnit


/**
 * Created by mzennis on 25/05/20.
 */
class PlayPusherTimer(val context: Context,
                      maxLiveStreamDuration: Long = DEFAULT_MAX_LIVE_STREAM_DURATION) {

    private var countUpTimer: PlayCountUpTimer? = null

    private var lastMillis: Long = maxLiveStreamDuration
    private var liveStreamDuration: Long = maxLiveStreamDuration

    private var callback: PlayPusherTimerListener? = null
    private var sharedPreferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

    fun addCallback(callback: PlayPusherTimerListener) {
        this.callback = callback
    }

    fun start() {
        liveStreamDuration = sharedPreferences?.getLong(PLAY_TIMER_LAST_STATE, liveStreamDuration)
                ?:DEFAULT_MAX_LIVE_STREAM_DURATION
        countUpTimer = getCountUpTimer(liveStreamDuration)
        countUpTimer?.start()
    }

    fun stop() {
        countUpTimer?.cancel()
        clearLastState()
        destroy()
    }

    fun pause() {
        countUpTimer?.cancel()
        saveLastState()
        destroy()
    }

    private fun destroy() {
        countUpTimer = null
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

    private fun getCountUpTimer(liveStreamDuration: Long): PlayCountUpTimer {
        return object : PlayCountUpTimer(liveStreamDuration, DEFAULT_INTERVAL) {
            override fun onFinish() {
                stop()
                callback?.onCountDownFinish()
            }

            override fun onTick(elapsedTime: Long) {
                lastMillis = millisUntilFinished(liveStreamDuration, elapsedTime)
                val millisToMinuteSecond = millisToMinuteSecond(elapsedTime)
                val minutes = millisToMinute(lastMillis)+1
                val seconds = millisToSecond(elapsedTime)
                callback?.onCountDownActive(millisToMinuteSecond,
                        minutesUntilFinished = minutes,
                        secondsUntilFinished = seconds)
            }
        }
    }

    private fun millisToMinuteSecond(millis: Long) = String.format("%02d:%02d",
            millisToMinute(millis),
            millisToSecond(millis)
    )

    private fun millisUntilFinished(liveStreamDuration: Long, elapsedTime: Long): Long = liveStreamDuration - elapsedTime

    private fun millisToMinute(millis: Long): Long = TimeUnit.MILLISECONDS.toMinutes(millis)
    private fun millisToSecond(millis: Long): Long = TimeUnit.MILLISECONDS.toSeconds(millis) -
            TimeUnit.MINUTES.toSeconds(millisToMinute(millis))

    companion object {
        const val DEFAULT_MAX_LIVE_STREAM_DURATION = 1800000L
        const val DEFAULT_INTERVAL = 1000L
        const val PLAY_TIMER_LAST_STATE = "play_broadcast_timer_last_state"
    }
}