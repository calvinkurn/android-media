package com.tokopedia.dev_monitoring_tools

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.anrwatchdog.ANRWatchDog
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext
import com.gu.toolargetool.Formatter
import com.gu.toolargetool.Logger
import com.gu.toolargetool.TooLargeTool
import com.gu.toolargetool.sizeTreeFromBundle
import com.tokopedia.dev_monitoring_tools.config.DevToolsRemoteConfig
import com.tokopedia.dev_monitoring_tools.session.UserJourney
import com.tokopedia.dev_monitoring_tools.toolargetool.TooLargeToolFormatter
import com.tokopedia.dev_monitoring_tools.toolargetool.TooLargeToolLogger
import timber.log.Timber

/**
 * Created by nathan on 9/16/17.
 */
object DevMonitoringUtils {

    @JvmStatic
    fun initCrashMonitoring() {
        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.w("P1#DEV_CRASH#'${UserJourney.getReadableJourneyActivity()}';error='${Log.getStackTraceString(throwable)}'")
            exceptionHandler.uncaughtException(thread, throwable)
        }
    }

    @JvmStatic
    fun initANRWatcher() {
        ANRWatchDog().setANRListener { anrError ->
            Timber.w("P1#DEV_ANR#'${UserJourney.getReadableJourneyActivity()}';anr='${Log.getStackTraceString(anrError)}'")
        }.start()
    }

    @JvmStatic
    fun initTooLargeTool(application: Application) {
        val minSizeLog = DevToolsRemoteConfig.getConfig(application).tooLargeToolMinSizeLog
        TooLargeTool.startLogging(application, TooLargeToolFormatter(minSizeLog), TooLargeToolLogger())
    }

    @JvmStatic
    fun initBlockCanary(context: Context) {
        BlockCanary.install(context, BlockCanaryContext()).start()
    }
}