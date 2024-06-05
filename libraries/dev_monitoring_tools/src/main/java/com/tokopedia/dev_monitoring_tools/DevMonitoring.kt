package com.tokopedia.dev_monitoring_tools

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.github.anrwatchdog.ANRWatchDog
import com.gu.toolargetool.TooLargeTool
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dev_monitoring_tools.anr.ANRListener
import com.tokopedia.dev_monitoring_tools.config.DevMonitoringToolsConfig
import com.tokopedia.dev_monitoring_tools.config.DevMonitoringToolsRemoteConfig
import com.tokopedia.dev_monitoring_tools.toolargetool.TooLargeToolFormatter
import com.tokopedia.dev_monitoring_tools.toolargetool.TooLargeToolLogger
import com.tokopedia.dev_monitoring_tools.userjourney.UserJourney
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import leakcanary.LeakCanary

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
            if (isCopyCrashToClipboardEnabled()) {
                configCopyCrashStackTraceToClipboard(throwable)
            }
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

    fun initLeakCanary(enable: Boolean = false) {
        DevMonitoringExtension.initLeakCanary(enable)
    }

    private fun configCopyCrashStackTraceToClipboard(throwable: Throwable) {
        val ctx = context
        val clipboard =
            ctx.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.let {
            val clip = ClipData.newPlainText("TkpdCrashLog", Log.getStackTraceString(throwable))
            it.setPrimaryClip(clip)
        }
        Thread {
            Looper.prepare()
            Toast.makeText(
                context,
                "App crashed. Stacktrace copied to clipboard.",
                Toast.LENGTH_LONG
            ).show()
            Looper.loop()
        }.start()
        Thread.sleep(3000)
    }

    private fun isCopyCrashToClipboardEnabled(): Boolean {
        val isRemoteConfigFtEnabled = DevMonitoringToolsRemoteConfig.isEnableCopyCrashStackTraceToClipboardFeature(context)
        return GlobalConfig.DEBUG && isRemoteConfigFtEnabled
    }
}
