package com.tokopedia.tokochat.config.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object CourierEventLogger {

    private const val TAG = "COURIER_EVENTS"
    private const val TAG_ERROR = "COURIER_ERROR"

    private const val EVENT_NAME = "EVENT_NAME"
    private const val EVENT_ERROR = "ERROR"

    fun logCourierEventToServerLogger(
        name: String,
        properties: Map<String, Any?>
    ) {
        val message = createMapEvent(name, properties)
        ServerLogger.log(Priority.P2, TAG, message)
    }

    fun logCourierErrorToServerLogger(throwable: Throwable) {
        ServerLogger.log(
            Priority.P2,
            TAG_ERROR,
            mapOf(EVENT_ERROR to throwable.message.orEmpty())
        )
    }

    private fun createMapEvent(
        name: String,
        properties: Map<String, Any?>
    ): Map<String, String> {
        val stringMap = properties.entries.associate { (key, value) ->
            key to value.toString()
        }
        val result: HashMap<String, String> = hashMapOf()
        result[EVENT_NAME] = name
        result.putAll(stringMap)
        return result
    }
}
