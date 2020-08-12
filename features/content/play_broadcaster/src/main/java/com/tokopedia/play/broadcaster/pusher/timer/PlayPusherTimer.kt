package com.tokopedia.play.broadcaster.pusher.timer

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.play.broadcaster.util.extension.convertMillisToMinuteSecond
import kotlin.math.max


/**
 * Created by mzennis on 25/05/20.
 */
class PlayPusherTimer(val context: Context) {

    constructor(context: Context,
                duration: Long) : this(context) {
        setupDuration(duration)
        this.mMaxDuration = mDuration
    }

    var callback: PlayPusherTimerListener? = null
    var timeoutList: List<Timeout> = Timeout.Default()
    var pauseDuration: Long? = null
    var mMaxDuration: Long = 0L

    private var mDuration: Long = 0L
    private var mRemainingMillis: Long = 0L

    private var mCountDownTimer: PlayCountDownTimer? = null
    private var mLocalStorage: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(context)

    fun start() {
        val lastMillis = getLongValue(REMAINING_MILLIS)?:0
        this.mDuration = if (lastMillis > 0) lastMillis else mDuration
        start(mDuration)
    }

    private fun start(duration: Long) {
        if (mCountDownTimer != null){
            mCountDownTimer?.cancel()
            mCountDownTimer = null
        }

        mCountDownTimer = getCountDownTimer(duration)
        mCountDownTimer?.start()
    }

    fun stop() {
        mCountDownTimer?.cancel()
        removeValue(REMAINING_MILLIS)
        removeValue(PAUSE_TIME)
    }

    fun restart(duration: Long) {
        setupDuration(duration)
        start(duration)
    }

    fun resume() {
        pauseDuration?.let { maxPauseMillis ->
            getLongValue(PAUSE_TIME)?.let { lastMillis ->
                if (lastMillis > 0  && reachMaximumPauseDuration(lastMillis, maxPauseMillis)) {
                    callback?.onReachMaximumPauseDuration()
                    removeValue(PAUSE_TIME)
                }
            }
        }
        mCountDownTimer?.resume()
    }

    fun pause() {
        mCountDownTimer?.pause()
        saveLongValue(REMAINING_MILLIS, mRemainingMillis)
        saveLongValue(PAUSE_TIME, System.currentTimeMillis())
    }

    fun destroy() {
        mCountDownTimer?.cancel()
        mCountDownTimer = null
    }

    private fun setupDuration(duration: Long) {
        this.mDuration = duration
        this.mDuration += 1000
        this.mRemainingMillis = mDuration
    }

    private fun getCountDownTimer(liveStreamDuration: Long): PlayCountDownTimer {
        return object : PlayCountDownTimer(liveStreamDuration, DEFAULT_INTERVAL) {
            override fun onFinish() {
                callback?.onCountDownFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                mRemainingMillis = millisUntilFinished
                val timeout = timeoutList.firstOrNull { millisUntilFinished in it.minMillis..it.maxMillis }
                if (timeout != null) callback?.onCountDownAlmostFinish(timeout.minute)
                else callback?.onCountDownActive(millisUntilFinished.convertMillisToMinuteSecond())
            }
        }
    }

    fun getTimeElapsed(): String = getTimeElapsedInMillis().convertMillisToMinuteSecond()

    private fun getTimeElapsedInMillis(): Long = max(0, mMaxDuration - mRemainingMillis)

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
        const val PAUSE_TIME = "play_broadcast_pause_time"
        const val REMAINING_MILLIS = "play_broadcast_remaining_millis"
    }
}