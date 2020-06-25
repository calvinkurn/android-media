package com.tokopedia.dev_monitoring_tools

import android.content.Context
import android.util.Log
import com.github.anrwatchdog.ANRWatchDog
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext
import timber.log.Timber

/**
 * Created by nathan on 9/16/17.
 */
object DevMonitoringUtils {

    @JvmStatic
    fun initCrashMonitoring() {
        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.w("P1#DEV_CRASH#'%s'", Log.getStackTraceString(throwable))
            exceptionHandler.uncaughtException(thread, throwable)
        }
    }

    @JvmStatic
    fun initANRWatcher() {
        ANRWatchDog().setANRListener { anrError -> Timber.w("P1#DEV_ANR'%s'", Log.getStackTraceString(anrError)) }.start()
    }

    @JvmStatic
    fun initBlockCanary(context: Context?) {
        BlockCanary.install(context, BlockCanaryContext()).start()
    }
}