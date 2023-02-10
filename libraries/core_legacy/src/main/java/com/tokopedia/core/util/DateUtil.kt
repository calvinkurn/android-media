package com.tokopedia.core.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private const val TIME_FORMAT = "ddmmyyhhmm"

    @JvmStatic
    fun getCurrentTime(): String {
        val currentSystemTime = Calendar.getInstance().time
        return SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(currentSystemTime)
    }
}
