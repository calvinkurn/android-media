package com.tokopedia.home.beranda.presentation.view.helper

import android.os.Handler
import java.util.*

/**
 * @author by yoasfs on 09/07/20
 */

fun runAutoRefreshJob(refreshCounterHandler: Handler,
                      runnableRefreshCounter: TimerRunnable) {
    startAutoRefreshCounter(refreshCounterHandler, runnableRefreshCounter)
}

fun stopAutoRefreshJob(refreshCounterHandler: Handler,
                       runnableRefreshCounter: TimerRunnable)  {
    stopAutoRefreshCounter(refreshCounterHandler, runnableRefreshCounter)
}

fun getAutoRefreshRunnableThread(serverTimeOffset: Long, expiredTime: Date,
                                 refreshCounterHandler: Handler,
                                 listener: HomeAutoRefreshListener) : TimerRunnable {
    return TimerRunnable(getServerRealTime(serverTimeOffset), expiredTime, serverTimeOffset, listener, refreshCounterHandler)
}

fun getServerRealTime(serverTimeOffset: Long): Date {
    val serverTime = Date(System.currentTimeMillis())
    serverTime.time = serverTime.time + serverTimeOffset
    return serverTime
}

private fun startAutoRefreshCounter(refreshCounterHandler: Handler, runnableRefreshCounter: TimerRunnable) {
    runnableRefreshCounter.start()
    refreshCounterHandler.post(runnableRefreshCounter)
}

private fun stopAutoRefreshCounter(refreshCounterHandler: Handler, runnableRefreshCounter: TimerRunnable) {
    runnableRefreshCounter.stop()
    refreshCounterHandler.removeCallbacks(runnableRefreshCounter)

}

fun isExpired(serverTime: Date, expiredTime: Date): Boolean {
    return serverTime.after(expiredTime)
}


data class TimerRunnable (
        private val serverTime: Date = Date(),
        private val expiredTime: Date = Date(),
        private val serverTimeOffset: Long = 0,
        private val listener: HomeAutoRefreshListener,
        private val refreshCounterHandler: Handler = Handler()) : Runnable {
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
