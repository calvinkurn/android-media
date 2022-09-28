package com.tokopedia.kotlin.extensions.view

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.formatTo(
    desiredOutputFormat: String,
    locale: Locale = Locale("id", "ID"),
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    return try {
        val outputFormat = SimpleDateFormat(desiredOutputFormat, locale)
        outputFormat.timeZone = timeZone
        val output = outputFormat.format(this)
        output
    } catch (e: Exception) {
        ""
    }
}

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}