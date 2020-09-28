package com.tokopedia.util.logger

import com.tokopedia.analytic.annotation.Level
import com.tokopedia.gtmutil.interfaces.GTMLogger
import timber.log.Timber

object GTMLoggerImpl : GTMLogger {
    override fun log(level: Level, info: String) {
        val stacktrace = StringBuilder()
        for (ste in Thread.currentThread().stackTrace) {
            stacktrace.append(String.format("%s\n", ste.toString()))
        }
        when (level) {
            Level.ERROR, Level.WARNING -> {
                Timber.w("P1#GTMLoggerImpl#error_or_warning_log;from='%s';current_thread='%s'", info, stacktrace.toString())
            }
            Level.IGNORE -> {
                Timber.w("P1#GTMLoggerImpl#ignored_log;current_thread='%s'", stacktrace.toString())
            }
        }
    }
}