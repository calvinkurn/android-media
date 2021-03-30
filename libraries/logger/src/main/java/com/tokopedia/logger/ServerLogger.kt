package com.tokopedia.logger

import com.tokopedia.logger.utils.Priority
import com.tokopedia.logger.utils.LoggerReporting

object ServerLogger {
    fun log(priority: Priority, tag: String, message: Map<String, String>) {
        val priorityText = when (priority) {
            Priority.P1 -> "P1"
            Priority.P2 -> "P2"
        }
        LoggerReporting.getInstance().log(priorityText, tag, message)
    }
}