package com.tokopedia.play.broadcaster.pusher.timer

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.util.concurrent.TimeUnit


/**
 * Created by mzennis on 25/05/20.
 */
class PlayPusherTimer(val context: Context) {

    constructor(context: Context,
                duration: Long) : this(context) {
        this.mDuration = duration
    }

    var callback: PlayPusherTimerListener? = null
    var timeoutList: List<Timeout> = Timeout.Default()
    var pauseDuration: Long? = null

    private var mDuration: Long = 0
    private var mLastTimeLeftInMillis: Long = 0

    private var mCountDownTimer: PlayCountDownTimer? = null
    private var mLocalStorage: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

    fun start() {
        if (mCountDownTimer != null){
            mCountDownTimer?.cancel()
            mCountDownTimer = null
        }

        mCountDownTimer = getCountDownTimer(mDuration)
        mCountDownTimer?.start()
    }

    fun stop() {
        mCountDownTimer?.cancel()
    }

    fun restart(duration: Long) {
        this.mDuration = duration
        start()
    }

    fun resume() {
        pauseDuration?.let { maxPauseMillis ->
            getLongValue(PLAY_TIMER_LAST_MILLIS)?.let { lastMillis ->
                if (reachMaximumPauseDuration(lastMillis, maxPauseMillis)) {
                    callback?.onReachMaximumPauseDuration()
                    removeValue(PLAY_TIMER_LAST_MILLIS)
                }
            }
        }
        mCountDownTimer?.resume()
    }

    fun pause() {
        mCountDownTimer?.pause()
        saveLongValue(PLAY_TIMER_LAST_MILLIS, System.currentTimeMillis())
    }

    fun destroy() {
        mCountDownTimer?.cancel()
        mCountDownTimer = null
    }

    private fun getCountDownTimer(liveStreamDuration: Long): PlayCountDownTimer {
        return object : PlayCountDownTimer(liveStreamDuration, DEFAULT_INTERVAL) {
            override fun onFinish() {
                callback?.onCountDownFinish(getTimeElapsed())
            }

            override fun onTick(millisUntilFinished: Long) {
                mLastTimeLeftInMillis = millisUntilFinished
                val timeout = timeoutList.firstOrNull { millisUntilFinished in it.minMillis..it.maxMillis }
                if (timeout != null) callback?.onCountDownAlmostFinish(timeout.minute)
                else callback?.onCountDownActive(millisToMinuteSecond(millisUntilFinished))
            }
        }
    }

    private fun getTimeElapsed(): String = millisToMinuteSecond(
            getTimeElapsedInMillis()
    )
    private fun getTimeElapsedInMillis(): Long = mDuration - mLastTimeLeftInMillis

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
        mLocalStorage?.edit()?.putLong(key, value)?.apply()
    }

    private fun getLongValue(key: String, defaultValue: Long = 0L): Long? =
            mLocalStorage?.getLong(key, defaultValue)

    private fun removeValue(key: String) {
        mLocalStorage?.edit()?.remove(key)?.apply()
    }


    companion object {
        const val DEFAULT_INTERVAL = 1000L
        const val PLAY_TIMER_LAST_MILLIS = "play_broadcast_last_millis"
    }
}