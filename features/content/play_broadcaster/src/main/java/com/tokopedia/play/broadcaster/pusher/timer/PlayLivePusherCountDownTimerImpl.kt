package com.tokopedia.play.broadcaster.pusher.timer

import android.os.CountDownTimer
import com.tokopedia.abstraction.common.utils.LocalCacheHandler


/**
 * Created by mzennis on 25/05/20.
 */
class PlayLivePusherCountDownTimerImpl(private val cacheHandler: LocalCacheHandler) : PlayLivePusherCountDownTimer {

    override val remainingDurationInMillis: Long
        get() = mRemainingMillis

    private var mCountDownTimer: CountDownTimer? = null
    private var mDuration: Long = 0L
    private var mRemainingMillis: Long = 0L

    private var mListener: PlayLivePusherCountDownTimerListener? = null

    override fun setDuration(duration: Long, maxDuration: Long) {
        setupDuration(duration)
    }

    override fun setListener(listener: PlayLivePusherCountDownTimerListener) {
        mListener = listener
    }

    override fun start() {
        start(mDuration)
    }

    override fun stop() {
        mCountDownTimer?.cancel()
        removeLastRemainingMillis()
    }

    override fun restart(duration: Long, maxDuration: Long) {
        setupDuration(duration)
        start(duration)
    }

    override fun resume() {
        val lastMillis = cacheHandler.getLong(KEY_REMAINING_MILLIS, 0L)
        restart(lastMillis, lastMillis)
    }

    override fun pause() {
        mCountDownTimer?.cancel()
        saveLastRemainingMillis()
    }

    override fun destroy() {
        mCountDownTimer?.cancel()
        mCountDownTimer = null
        mListener = null
    }

    private fun setupDuration(duration: Long) {
        this.mDuration = duration
        this.mRemainingMillis = mDuration
    }

    private fun start(duration: Long) {
        if (mCountDownTimer != null) {
            mCountDownTimer?.cancel()
            mCountDownTimer = null
        }

        mCountDownTimer = getCountDownTimer(duration)
        mCountDownTimer?.start()
    }

    private fun removeLastRemainingMillis() {
        cacheHandler.remove(KEY_REMAINING_MILLIS)
    }

    private fun getCountDownTimer(liveStreamDuration: Long): CountDownTimer {
        return object : CountDownTimer(liveStreamDuration, DEFAULT_INTERVAL) {
            override fun onFinish() {
                mListener?.onCountDownTimerFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                mRemainingMillis = millisUntilFinished
                mListener?.onCountDownTimerActive(millisUntilFinished)
            }
        }
    }

    private fun saveLastRemainingMillis() {
        cacheHandler.putLong(KEY_REMAINING_MILLIS, mRemainingMillis)
        cacheHandler.applyEditor()
    }

    companion object {
        const val DEFAULT_INTERVAL = 1000L

        const val KEY_REMAINING_MILLIS = "play_broadcast_remaining_millis"
    }
}