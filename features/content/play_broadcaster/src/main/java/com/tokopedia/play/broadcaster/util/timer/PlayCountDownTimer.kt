package com.tokopedia.play.broadcaster.util.timer

import android.os.CountDownTimer
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.broadcaster.util.extension.convertMillisToMinuteSecond
import javax.inject.Inject
import kotlin.math.max


/**
 * Created by mzennis on 25/05/20.
 */
class PlayCountDownTimer @Inject constructor(
    private val localCacheHandler: LocalCacheHandler
    ) {

    val timeElapsed: String
        get() = getTimeElapsedInMillis().convertMillisToMinuteSecond()

    private var mCountDownTimer: CountDownTimer? = null
    private var mMaxDuration: Long = 0L
    private var mDuration: Long = 0L
    private var mRemainingMillis: Long = 0L

    private var mListener: Listener? = null
    private var mTimeoutList = defaultCountDownTimeoutConfig()

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
        localCacheHandler.remove(KEY_REMAINING_MILLIS)
    }

    fun restart(duration: Long) {
        setupDuration(duration)
        start(duration)
    }

    fun resume() {
        val lastMillis = localCacheHandler.getLong(KEY_REMAINING_MILLIS, 0L)
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
        localCacheHandler.putLong(KEY_REMAINING_MILLIS, mRemainingMillis)
    }

    companion object {
        const val DEFAULT_INTERVAL = 1000L

        const val KEY_REMAINING_MILLIS = "play_broadcast_remaining_millis"
    }

    interface Listener {

        fun onCountDownActive(millis: Long)
        fun onCountDownAlmostFinish(minutes: Long)
        fun onCountDownFinish()
    }
}