package com.tokopedia.shop.flashsale.common.extension

import com.tokopedia.shop.flashsale.common.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

private const val ONE_MONTH_OFFSET = 1

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

fun Date.advanceDayBy(days: Int): Date {
    val now = Calendar.getInstance()
    now.time = this
    now.add(Calendar.DAY_OF_YEAR, days)
    return now.time
}

fun Date.advanceMonthBy(months: Int): Date {
    val now = Calendar.getInstance()
    now.time = this
    now.add(Calendar.MONTH, months)
    return now.time
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

fun Date.decreaseMinuteBy(desiredMinuteBeDecreased: Int): Date {
    val now = Calendar.getInstance()
    now.time = this
    now.add(Calendar.MINUTE, -desiredMinuteBeDecreased)
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

fun Date.extractMonth(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MONTH) + ONE_MONTH_OFFSET
}

fun Date.extractYear(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.YEAR)
}

fun Date.localFormatTo(
    desiredOutputFormat: String,
    locale: Locale = LocaleConstant.INDONESIA
): String {
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
    calendar.timeInMillis = this.time + calendar.timeZoneOffsetInMillis
    return calendar.time
}

/**
 * This method will return time-zone offset in millis,
 * calculation based on recommended formula from now deprecated [Date.getTimezoneOffset]:
 *
 * > replaced by -(Calendar.get(Calendar.ZONE_OFFSET) + Calendar.get(Calendar.DST_OFFSET)) / (60 * 1000)
 *
 * However we need it in millis, so no need to divide with (60 * 1000) anymore.
 *
 * @return the time-zone offset, in millis, for the current time zone.
 */
private val Calendar.timeZoneOffsetInMillis: Int
    get() = -(get(Calendar.ZONE_OFFSET) + get(Calendar.DST_OFFSET))

fun Date.minuteDifference(): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val now = Calendar.getInstance()

    val differenceInMillis = calendar.timeInMillis - now.timeInMillis

    return TimeUnit.MILLISECONDS.toMinutes(differenceInMillis)
}

fun Date.daysDifference(date : Date): Long {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val differenceInMillis = calendar.timeInMillis - date.time

    return TimeUnit.MILLISECONDS.toDays(differenceInMillis)
}