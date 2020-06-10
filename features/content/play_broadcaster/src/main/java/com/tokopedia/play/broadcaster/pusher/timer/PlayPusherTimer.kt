package com.tokopedia.play.broadcaster.pusher.timer

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.preference.PreferenceManager
import java.util.concurrent.TimeUnit


/**
 * Created by mzennis on 25/05/20.
 */
class PlayPusherTimer(val context: Context,
                      private var liveStreamDuration: Long,
                      private var callback: PlayPusherTimerListener) {

    private var countDownTimer: CountDownTimer? = null

    private var lastMillis: Long = liveStreamDuration

    private var sharedPreferences: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

    fun start() {
        var liveStreamDuration = sharedPreferences?.getLong(PLAY_TIMER_LAST_STATE, liveStreamDuration)
                ?:DEFAULT_MAX_LIVE_STREAM_DURATION
        liveStreamDuration += DEFAULT_INTERVAL
        countDownTimer = getCountDownTimer(liveStreamDuration)
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

    private fun onCountDownTimerActive(millisUntilFinished: Long) {
        lastMillis = millisUntilFinished

        when {
            fiveMinutesLeft(millisUntilFinished) -> callback.onCountDownAlmostFinish(FIVE_MINUTES)
            twoMinutesLeft(millisUntilFinished) -> callback.onCountDownAlmostFinish(TWO_MINUTES)
            else -> callback.onCountDownActive(millisToMinuteSecond(millisUntilFinished))
        }
    }

    private fun onCountDownTimerFinish() {
        callback.onCountDownFinish()
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

    private fun getCountDownTimer(liveStreamDuration: Long): CountDownTimer {
        return object : CountDownTimer(liveStreamDuration, DEFAULT_INTERVAL) {
            override fun onFinish() {
                onCountDownTimerFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
               onCountDownTimerActive(millisUntilFinished)
            }
        }
    }

    private fun twoMinutesLeft(millisUntilFinished: Long): Boolean =
            millisUntilFinished in 119_600..121_000

    private fun fiveMinutesLeft(millisUntilFinished: Long): Boolean =
            millisUntilFinished in 296_000..301_000

    private fun millisToMinuteSecond(millis: Long) = String.format("%02d:%02d",
            millisToMinute(millis),
            millisToSecond(millis)
    )

    private fun millisToMinute(millis: Long): Long = TimeUnit.MILLISECONDS.toMinutes(millis)
    private fun millisToSecond(millis: Long): Long = TimeUnit.MILLISECONDS.toSeconds(millis) -
            TimeUnit.MINUTES.toSeconds(millisToMinute(millis))

    companion object {
        const val DEFAULT_MAX_LIVE_STREAM_DURATION = 1800000L
        const val DEFAULT_INTERVAL = 1000L
        const val FIVE_MINUTES = 5L
        const val TWO_MINUTES = 2L
        const val PLAY_TIMER_LAST_STATE = "play_broadcast_timer_last_state"
    }
}