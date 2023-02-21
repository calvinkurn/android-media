package com.tokopedia.media.picker.analytics

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

// log tag
const val TAG_MEDIA_PICKER = "MEDIA_PICKER"

sealed class LogType(val value: String) {
    object NoInternetConnection : LogType("no_connection")
    object BitmapConverter : LogType("bitmap_converter")
}

object Logger {

    fun send(startTime: Long, endTime: Long) {
        send(LogType.BitmapConverter, mapOf(
            "start_time" to startTime.toString(),
            "end_time" to endTime.toString()
        ))
    }

    fun send(type: LogType, message: Map<String, String> = mapOf()) {
        ServerLogger.log(
            Priority.P1,
            TAG_MEDIA_PICKER,
            mutableMapOf("type" to type.value).also {
                if (message.isNotEmpty()) {
                    it.putAll(message)
                }
            }
        )
    }
}
