package com.tokopedia.play.broadcaster.pusher.timer

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.util.concurrent.TimeUnit


/**
 * Created by mzennis on 25/05/20.
 */
class PlayPusherTimer(val context: Context,
                      private val duration: Long,
                      private var callback: PlayPusherTimerListener) {

    var timeoutList: List<Timeout> = Timeout.Default()
    var pauseDuration: Long? = null

    private var lastTimeLeftInMillis: Long = 0

    private var countDownTimer: PlayCountDownTimer = getCountDownTimer(duration)
    private var localStorage: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

    fun start() {
        countDownTimer.start()
    }

    fun stop() {
        callback.onCountDownFinish(getTimeElapsed())
        countDownTimer.cancel()
    }

    fun resume() {
        pauseDuration?.let { maxPauseMillis ->
            getLongValue(PLAY_TIMER_LAST_MILLIS)?.let { lastMillis ->
                if (reachMaximumPauseDuration(lastMillis, maxPauseMillis)) {
                    callback.onReachMaximumPauseDuration()
                    removeValue(PLAY_TIMER_LAST_MILLIS)
                }
            }
        }
        countDownTimer.resume()
    }

    fun pause() {
        countDownTimer.pause()
        saveLongValue(PLAY_TIMER_LAST_MILLIS, System.currentTimeMillis())
    }

    private fun getCountDownTimer(liveStreamDuration: Long): PlayCountDownTimer {
        return object : PlayCountDownTimer(liveStreamDuration, DEFAULT_INTERVAL) {
            override fun onFinish() {
                callback.onCountDownFinish(getTimeElapsed())
            }

            override fun onTick(millisUntilFinished: Long) {
                lastTimeLeftInMillis = millisUntilFinished
                val timeout = timeoutList.firstOrNull { millisUntilFinished in it.minMillis..it.maxMillis }
                if (timeout != null) callback.onCountDownAlmostFinish(timeout.minute)
                else callback.onCountDownActive(millisToMinuteSecond(millisUntilFinished))
            }
        }
    }

    private fun getTimeElapsed(): String = millisToMinuteSecond(
            getTimeElapsedInMillis()
    )
    private fun getTimeElapsedInMillis(): Long = duration - lastTimeLeftInMillis

    private fun millisToMinuteSecond(millis: Long) = String.format("%02d:%02d",
            millisToMinute(millis),
            millisToSecond(millis)
    )

    private fun millisToMinute(millis: Long): Long = TimeUnit.MILLISECONDS.toMinutes(millis)
    private fun millisToSecond(millis: Long): Long = TimeUnit.MILLISECONDS.toSeconds(millis) -
            TimeUnit.MINUTES.toSeconds(millisToMinute(millis))

    private fun reachMaximumPauseDuration(lastMillis: Long, maxPauseMillis: Long): Boolean {
        val currentMillis = System.currentTimeMillis()
        return ((currentMillis - lastMillis) > maxPauseMillis)
    }

    /**
     * Store
     */
    private fun saveLongValue(key: String, value: Long) {
        localStorage?.edit()?.putLong(key, value)?.apply()
    }

    private fun getLongValue(key: String, defaultValue: Long = 0L): Long? =
            localStorage?.getLong(key, defaultValue)

    private fun removeValue(key: String) {
        localStorage?.edit()?.remove(key)?.apply()
    }


    companion object {
        const val DEFAULT_INTERVAL = 1000L
        const val PLAY_TIMER_LAST_MILLIS = "play_broadcast_last_millis"
    }
}