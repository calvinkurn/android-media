package com.tokopedia.campaign.utils.extension

import com.tokopedia.campaign.utils.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.formatTo(
    desiredOutputFormat: String,
    locale: Locale = LocaleConstant.INDONESIA,
    timeZone: TimeZone = TimeZone.getTimeZone("GMT")
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