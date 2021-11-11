package com.tokopedia.analyticsdebugger.sse.util

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
object DateTimeUtil {
    fun formatDate(timestamp: Long, outputPattern: String = "dd MMM yyyy HH:mm:ss.SSS"): String {
        return try {
            val output = SimpleDateFormat(outputPattern, Locale.getDefault())
            output.format(Date(timestamp))
        }
        catch (e: Exception) {
            ""
        }
    }
}