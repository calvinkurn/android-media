package com.tokopedia.dev_monitoring_tools

import android.app.Application
import android.content.Context
import android.util.Log
import com.github.anrwatchdog.ANRWatchDog
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext
import com.gu.toolargetool.TooLargeTool
import com.tokopedia.dev_monitoring_tools.anr.ANRListener
import com.tokopedia.dev_monitoring_tools.config.DevMonitoringToolsConfig
import com.tokopedia.dev_monitoring_tools.config.DevMonitoringToolsRemoteConfig
import com.tokopedia.dev_monitoring_tools.userjourney.UserJourney
import com.tokopedia.dev_monitoring_tools.toolargetool.TooLargeToolFormatter
import com.tokopedia.dev_monitoring_tools.toolargetool.TooLargeToolLogger
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

/**
 * Created by nathan on 9/16/17.
 */
class DevMonitoring(private var context: Context) {
    private var devMonitoringToolsConfig = DevMonitoringToolsConfig()

    init {
        devMonitoringToolsConfig = DevMonitoringToolsRemoteConfig.getConfig(context)
    }

    fun initCrashMonitoring() {
        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            ServerLogger.log(Priority.P1, "DEV_CRASH", mapOf("journey" to UserJourney.getReadableJourneyActivity(devMonitoringToolsConfig.userJourneySize),
                    "error" to Log.getStackTraceString(throwable).replace("\n", "").replace("\t", " ")))
            exceptionHandler?.uncaughtException(thread, throwable)
        }
    }

    fun initANRWatcher() {
        ANRWatchDog().setANRListener(ANRListener(devMonitoringToolsConfig.anrIgnoreList, devMonitoringToolsConfig.userJourneySize)).start()
    }

    fun initTooLargeTool(application: Application) {
        val minSizeLog = devMonitoringToolsConfig.tooLargeToolMinSizeLog
        TooLargeTool.startLogging(application, TooLargeToolFormatter(minSizeLog, devMonitoringToolsConfig.userJourneySize), TooLargeToolLogger())
    }

    fun initBlockCanary() {
        BlockCanary.install(context, BlockCanaryContext()).start()
    }
}