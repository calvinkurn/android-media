package com.tokopedia.content.common.util

import android.os.CountDownTimer

/**
 * Created by kenny.hadisaputra on 03/07/23
 */
/**
 * An implementation of CountDownTimer that can be paused, it uses [android.os.CountDownTimer] internally.
 * The internal instance of [android.os.CountDownTimer] will be re-created if we resume the timer after it has been paused,
 * but it will maintain the remaining millisUntilFinished
 *
 * The function is made to be as similar as possible to [android.os.CountDownTimer]
 */
abstract class CountDownTimer2(
    private val millisInFuture: Long,
    private val countDownInterval: Long
) {

    private var millisRemaining: Long = millisInFuture

    private var mTimer: CountDownTimer? = null

    fun start() = synchronized(this) {
        if (mTimer == null) {
            val newTimer = createTimer()
            mTimer = newTimer
            newTimer.start()
        }
    }

    fun release() = synchronized(this) {
        pause()
        millisRemaining = millisInFuture
    }

    fun pause() = synchronized(this) {
        mTimer?.cancel()
        mTimer = null
    }

    private fun createTimer(): CountDownTimer {
        return object : CountDownTimer(millisRemaining, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                this@CountDownTimer2.onTick(millisUntilFinished)
                millisRemaining = millisUntilFinished
            }

            override fun onFinish() {
                this@CountDownTimer2.onFinish()
            }
        }
    }

    abstract fun onTick(millisUntilFinished: Long)

    abstract fun onFinish()
}
