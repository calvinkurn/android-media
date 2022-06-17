package com.tokopedia.shop.flashsale.common.extension

import com.tokopedia.shop.flashsale.common.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val WIB_TIME_OFFSET = 7

fun Date.formatTo(desiredOutputFormat: String, locale: Locale = LocaleConstant.INDONESIA): String {
    return try {
        val outputFormat = SimpleDateFormat(desiredOutputFormat, locale)
        outputFormat.timeZone = TimeZone.getTimeZone("GMT")
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

fun Date.advanceHourBy(hour: Int): Date {
    val now = Calendar.getInstance()
    now.time = this
    now.add(Calendar.HOUR_OF_DAY, hour)
    return now.time
}

fun Date.advanceMinuteBy(minute: Int): Date {
    val now = Calendar.getInstance()
    now.time = this
    now.add(Calendar.MINUTE, minute)
    return now.time
}


fun Date.decreaseHourBy(desiredHourToBeDecreased: Int): Date {
    val now = Calendar.getInstance()
    now.time = this
    now.add(Calendar.HOUR_OF_DAY, -desiredHourToBeDecreased)
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

fun Date.hourOnly(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun Date.minuteOnly(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun Date.extractHour(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun Date.extractMinute(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MINUTE)
}

fun Date.localFormatTo(desiredOutputFormat: String, locale: Locale = LocaleConstant.INDONESIA): String {
    return try {
        val outputFormat = SimpleDateFormat(desiredOutputFormat, locale)
        val output = outputFormat.format(this)
        output
    } catch (e: Exception) {
        ""
    }
}

fun Date.removeTimeZone(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.HOUR_OF_DAY, -WIB_TIME_OFFSET)
    return calendar.time
}

fun Date.minuteDifference(): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val now = Calendar.getInstance()

    val differenceInMillis = calendar.timeInMillis - now.timeInMillis

    val diff = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis)
    return diff
}
