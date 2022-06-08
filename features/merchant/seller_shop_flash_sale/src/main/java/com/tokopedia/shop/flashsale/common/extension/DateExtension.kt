package com.tokopedia.shop.flashsale.common.extension

import com.tokopedia.shop.flashsale.common.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*

private const val EMPTY_STRING = ""

fun Date.formatTo(desiredOutputFormat: String, locale: Locale = LocaleConstant.INDONESIA): String {
    return try {
        val outputFormat = SimpleDateFormat(desiredOutputFormat, locale)
        outputFormat.timeZone = TimeZone.getTimeZone("GMT")
        val output = outputFormat.format(this)
        output
    } catch (e: Exception) {
        EMPTY_STRING
    }
}

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}

fun Date.decreaseHourBy(desiredHourToBeDecreased: Int): Date {
    val now = Calendar.getInstance()
    now.time = this
    now.add(Calendar.HOUR_OF_DAY, desiredHourToBeDecreased)
    return now.time
}

fun Date.dateOnly(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}
