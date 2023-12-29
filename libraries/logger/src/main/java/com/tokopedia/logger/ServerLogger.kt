package com.tokopedia.logger

import com.tokopedia.logger.utils.Priority

object ServerLogger {
    @JvmStatic
    fun log(priority: Priority, tag: String, message: Map<String, String>) {
        LogManager.log(priority, tag, message)
    }

    /**
     * This function is basically exist for Hansel Purpose.
     */
    @JvmStatic
    fun logP1(tag: String, message: Map<String, String>) {
        LogManager.log(Priority.P1, tag, message)
    }

    /**
     * This function is basically exist for Hansel Purpose.
     */
    @JvmStatic
    fun logP2(tag: String, message: Map<String, String>) {
        LogManager.log(Priority.P2, tag, message)
    }
}
