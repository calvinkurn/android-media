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
const val OS_FORMAT_DATE = "yyyy-MM-dd'T'HH:mm:ssXXX"

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

// default is to locale timeZone
fun toReadableString(format: String, dateUTC: Date): String {
    val fromFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
    try {
        return fromFormat.format(dateUTC)
    } catch (e: Exception) {
        return dateUTC.toString()
    }

}

fun toReadableString(format: String, unixTimeSecondsUTC: String): String {
    return toReadableString(format, unixTimeSecondsUTC.toLong())
}

fun toReadableString(format: String, unixTimeSecondsUTC: Long): String {
    try {
        return toReadableString(format, Date(unixTimeSecondsUTC * 1000L))
    } catch (e: Exception) {
        return unixTimeSecondsUTC.toString()
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

fun dateFormatToBeReadable(date: String, datePattern: String, outputPattern: String): String? {
    val dateFormat = SimpleDateFormat(datePattern, DEFAULT_LOCALE)
    val output = SimpleDateFormat(outputPattern, DEFAULT_LOCALE)
    return try {
        val d: Date? = dateFormat.parse(date)
        d?.let { output.format(d) }
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}