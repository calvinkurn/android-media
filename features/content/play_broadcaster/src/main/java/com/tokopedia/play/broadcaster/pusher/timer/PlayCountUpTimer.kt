package com.tokopedia.play.broadcaster.pusher.timer

import android.os.Handler
import android.os.Message
import android.os.SystemClock

/**
 * Created by mzennis on 04/06/20.
 * Inspired by {@link android.os.CountDownTimer}
 */
abstract class PlayCountUpTimer(millisInFuture: Long, countDownInterval: Long) {

    private val mMillisInFuture: Long = millisInFuture
    private val mInterval: Long = countDownInterval
    private var mStopTimeInFuture: Long = 0
    private var mBase: Long = 0
    private var mCancelled = false

    @Synchronized
    fun cancel() {
        mCancelled = true
        mHandler.removeMessages(MSG)
    }

    @Synchronized
    fun start(): PlayCountUpTimer {
        mCancelled = false
        if (mMillisInFuture <= 0) {
            onFinish()
            return this
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        mBase = SystemClock.elapsedRealtime()
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        return this
    }

    abstract fun onTick(elapsedTime: Long, millisUntilFinished: Long)

    abstract fun onFinish()

    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            synchronized(this@PlayCountUpTimer) {
                if (mCancelled) {
                    return
                }
                val millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime()
                val elapsedTime = SystemClock.elapsedRealtime() - mBase
                if (millisLeft <= 0) {
                    onFinish()
                } else {
                    val lastTickStart = SystemClock.elapsedRealtime()
                    onTick(elapsedTime, millisLeft)
                    val lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart
                    var delay: Long
                    if (millisLeft < mInterval) {
                        delay = millisLeft - lastTickDuration
                        if (delay < 0) delay = 0
                    } else {
                        delay = mInterval - lastTickDuration
                        while (delay < 0) delay += mInterval
                    }
                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }
    }

    companion object {
        private const val MSG = 1
    }
}