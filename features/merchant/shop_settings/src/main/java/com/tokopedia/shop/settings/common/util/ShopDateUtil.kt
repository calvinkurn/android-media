@file:JvmName("ShopDateUtil")

package com.tokopedia.shop.settings.common.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by hendry on 15/08/18.
 */
val DEFAULT_LOCALE = Locale("in", "ID")
const val FORMAT_DAY_DATE = "EEE, dd MMM yyyy"
const val FORMAT_DATE = "dd MMM yyyy"
const val FORMAT_DATE_TIME = "dd MMM yyyy, 'pukul' HH:mm"

val currentCalendar: Calendar
    get() = Calendar.getInstance()

val currentDate: Date
    get() {
        val now = currentCalendar
        return now.time
    }

val tomorrowDate: Date
    get() = getCurrentDateWithOffset(1)

fun getCurrentDateWithOffset(dayOffset: Int): Date {
    val cal = currentCalendar
    cal.add(Calendar.DATE, dayOffset)
    return cal.time
}

fun stringToDate(format: String, input: String): Date {
    val fromFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
    try {
        return fromFormat.parse(input)
    } catch (e: ParseException) {
        e.printStackTrace()
        throw RuntimeException("Date doesnt valid ($input)")
    }

}

fun toReadableString(format: String, date: Date): String {
    val fromFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
    try {
        return fromFormat.format(date)
    } catch (e: Exception) {
        return date.toString()
    }

}

fun toReadableString(format: String, unixTimeSeconds: String): String {
    try {
        return toReadableString(format, Date(java.lang.Long.parseLong(unixTimeSeconds) * 1000L))
    } catch (e: Exception) {
        return unixTimeSeconds
    }
}

fun toDate(year: Int, month: Int, dayOfMonth: Int): Date {
    try {
        val now = currentCalendar
        now.set(Calendar.YEAR, year)
        now.set(Calendar.MONTH, month)
        now.set(Calendar.DATE, dayOfMonth)
        return now.time
    } catch (e: Exception) {
        return currentDate
    }

}

fun unixToDate(unixTimeMs: Long): Date {
    try {
        return Date(unixTimeMs)
    } catch (e: Exception) {
        return currentDate
    }

}
