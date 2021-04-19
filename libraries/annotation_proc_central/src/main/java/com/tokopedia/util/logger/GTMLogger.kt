package com.tokopedia.util.logger

import com.tokopedia.analytic.annotation.Level
import com.tokopedia.gtmutil.interfaces.GTMLogger
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import timber.log.Timber

object GTMLoggerImpl : GTMLogger {
    override fun log(level: Level, info: String) {
        val stacktrace = StringBuilder()
        for (ste in Thread.currentThread().stackTrace) {
            stacktrace.append(String.format("%s\n", ste.toString()))
        }
        when (level) {
            Level.ERROR, Level.WARNING -> {
                ServerLogger.log(Priority.P1, "GTMLoggerImpl", mapOf("type" to "error_or_warning_log",
                        "from" to info, "current_thread" to stacktrace.toString()
                ))
            }
            Level.IGNORE -> {
                ServerLogger.log(Priority.P1, "GTMLoggerImpl", mapOf("type" to "ignored_log",
                        "current_thread" to stacktrace.toString()
                ))
            }
        }
    }
}