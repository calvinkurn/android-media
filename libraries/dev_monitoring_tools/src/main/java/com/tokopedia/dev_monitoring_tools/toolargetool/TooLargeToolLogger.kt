package com.tokopedia.dev_monitoring_tools.toolargetool

import android.util.Log
import com.gu.toolargetool.Logger
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

class TooLargeToolLogger : Logger {

    override fun log(msg: String) {
        if (!msg.isBlank()) {
            ServerLogger.log(Priority.P1, "DEV_TOO_LARGE", mapOf("type" to "warning", "message" to msg))
        }
    }

    override fun logException(e: Exception) {
        ServerLogger.log(Priority.P1, "DEV_TOO_LARGE", mapOf("type" to "exception", "err" to Log.getStackTraceString(e)))
    }
}