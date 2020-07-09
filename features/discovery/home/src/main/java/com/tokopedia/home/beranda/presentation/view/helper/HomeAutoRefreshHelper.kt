package com.tokopedia.home.beranda.presentation.view.helper

import android.os.Handler
import com.tokopedia.design.countdown.CountDownView
import java.util.*

/**
 * @author by yoasfs on 09/07/20
 */

fun runAutoRefreshJob(serverTimeOffset: Long, expiredTime: Date,
              refreshCounterHandler: Handler,
              listener: HomeAutoRefreshListener) {
    val serverTime = Date(System.currentTimeMillis())
    serverTime.time = serverTime.time + serverTimeOffset
    if (isExpired(serverTime, expiredTime)) {
        handleExpiredTime(listener)
        return
    }
    startAutoRefreshCounter(
            refreshCounterHandler,
            TimerRunnable(serverTime, expiredTime, serverTimeOffset, listener, refreshCounterHandler))
}

private fun startAutoRefreshCounter(refreshCounterHandler: Handler, runnableRefreshCounter: TimerRunnable) {
    runnableRefreshCounter.start()
    refreshCounterHandler.post(runnableRefreshCounter)
}

private fun isExpired(serverTime: Date, expiredTime: Date): Boolean {
    return serverTime.after(expiredTime)
}


class TimerRunnable (
        private val serverTime: Date,
        private val expiredTime: Date,
        private val serverTimeOffset: Long,
        private val listener: HomeAutoRefreshListener,
        private val refreshCounterHandler: Handler) : Runnable {
    private var stop = false
    fun stop() {
        stop = true
    }

    fun start() {
        stop = false
    }

    override fun run() {
        if (!isExpired(serverTime, expiredTime) && !stop) {
            val currentDate = Date()
            val currentMillisecond = currentDate.time + serverTimeOffset
            serverTime.time = currentMillisecond
            refreshCounterHandler.postDelayed(this, 1000.toLong())
        } else {
            handleExpiredTime(listener)
        }
    }

}

private fun handleExpiredTime(listener: HomeAutoRefreshListener) {
    listener.onHomeAutoRefreshTriggered()
}

interface HomeAutoRefreshListener {
    fun onHomeAutoRefreshTriggered()
}
