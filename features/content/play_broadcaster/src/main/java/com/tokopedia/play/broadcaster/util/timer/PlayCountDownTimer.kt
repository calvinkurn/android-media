package com.tokopedia.play.broadcaster.util.timer

import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import com.tokopedia.play.broadcaster.util.extension.convertMillisToMinuteSecond
import javax.inject.Inject
import kotlin.math.max


/**
 * Created by mzennis on 25/05/20.
 */
class PlayCountDownTimer @Inject constructor(private val context: Context) {

    val maxDuration: Long
        get() = mMaxDuration

    val timeElapsed: String
        get() = getTimeElapsedInMillis().convertMillisToMinuteSecond()

    private var mCountDownTimer: CountDownTimer? = null
    private var mMaxDuration: Long = 0L
    private var mDuration: Long = 0L
    private var mRemainingMillis: Long = 0L

    private var mListener: Listener? = null
    private var mTimeoutList = defaultCountDownTimeoutConfig()

    private val localStorage: SharedPreferences
        get() = context.getSharedPreferences(PLAY_BROADCAST_PREFERENCE, Context.MODE_PRIVATE)

    private val localStorageEditor: SharedPreferences.Editor
        get() = localStorage.edit()

    fun setDuration(duration: Long) {
        setupDuration(duration)
        setMaxDuration(duration)
    }

    fun setMaxDuration(duration: Long) {
        mMaxDuration = duration
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun start() {
        start(mDuration)
    }

    private fun start(duration: Long) {
        if (mCountDownTimer != null) {
            mCountDownTimer?.cancel()
            mCountDownTimer = null
        }

        mCountDownTimer = getCountDownTimer(duration)
        mCountDownTimer?.start()
    }

    fun stop() {
        mCountDownTimer?.cancel()
        removeLastRemainingMillis()
    }

    private fun removeLastRemainingMillis() {
        localStorageEditor.remove(KEY_REMAINING_MILLIS)?.apply()
    }

    fun restart(duration: Long) {
        setupDuration(duration)
        start(duration)
    }

    fun resume() {
        val lastMillis = localStorage.getLong(KEY_REMAINING_MILLIS, 0L)
        restart(lastMillis)
    }

    fun pause() {
        mCountDownTimer?.cancel()
        saveLastRemainingMillis()
    }

    fun destroy() {
        mCountDownTimer?.cancel()
        mCountDownTimer = null
        mListener = null
    }

    private fun setupDuration(duration: Long) {
        this.mDuration = duration
        this.mRemainingMillis = mDuration
    }

    private fun getCountDownTimer(liveStreamDuration: Long): CountDownTimer {
        return object : CountDownTimer(liveStreamDuration, DEFAULT_INTERVAL) {
            override fun onFinish() {
                mListener?.onCountDownFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                mRemainingMillis = millisUntilFinished
                val timeout = mTimeoutList.firstOrNull { millisUntilFinished in it.minMillis..it.maxMillis }
                if (timeout != null) mListener?.onCountDownAlmostFinish(timeout.minute)
                else mListener?.onCountDownActive(millisUntilFinished)
            }
        }
    }

    private fun getTimeElapsedInMillis(): Long = max(0, mMaxDuration - mRemainingMillis)

    private fun defaultCountDownTimeoutConfig() = arrayListOf(
            CountDownTimeout(2),
            CountDownTimeout(5)
    )

    private fun saveLastRemainingMillis() {
        localStorageEditor.putLong(KEY_REMAINING_MILLIS, mRemainingMillis)?.apply()
    }

    companion object {
        const val DEFAULT_INTERVAL = 1000L

        const val PLAY_BROADCAST_PREFERENCE = "play_broadcast_timer"
        const val KEY_REMAINING_MILLIS = "play_broadcast_remaining_millis"
    }

    interface Listener {

        fun onCountDownActive(millis: Long)
        fun onCountDownAlmostFinish(minutes: Long)
        fun onCountDownFinish()
    }
}