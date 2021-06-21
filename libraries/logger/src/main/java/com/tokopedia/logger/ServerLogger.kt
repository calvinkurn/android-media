package com.tokopedia.logger

import com.tokopedia.logger.utils.Priority
import com.tokopedia.logger.utils.LoggerReporting

object ServerLogger {
    @JvmStatic
    fun log(priority: Priority, tag: String, message: Map<String, String>) {
        LogManager.log(priority, tag, message)
    }
}