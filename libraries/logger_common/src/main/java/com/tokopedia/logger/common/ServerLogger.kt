package com.tokopedia.logger.common

import timber.log.Timber

object ServerLogger {
    fun sendLog(priority: Priority, tag: String, dataException: Map<String, String>) {
        val priorityText = when(priority) {
            Priority.P1 -> "P1"
            Priority.P2 -> "P2"
        }
        Timber.w(LoggerException(priorityText, tag, dataException))
    }
}