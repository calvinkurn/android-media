package com.tokopedia.kotlin.extensions

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*


val Date.relativeWeekDay: String
    get() {
        val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val calActive = Calendar.getInstance()
        calActive.timeInMillis = time

        val now = Calendar.getInstance()
        return if (DateUtils.isToday(time)) "Hari Ini"
        else if (now.get(Calendar.DATE) - calActive.get(Calendar.DATE) == 1) "Kemarin"
        else format.format(this)
    }

fun Date.toFormattedString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

// return n days interval in formatted
fun getCalculatedFormattedDate(format: String, days: Int, locale: Locale = Locale.getDefault()): String {
    val cal = Calendar.getInstance()
    val formatter = SimpleDateFormat(format, locale)
    cal.add(Calendar.DAY_OF_YEAR, days)
    return formatter.format(Date(cal.timeInMillis))
}

// return month name in 3 characters
fun convertMonth(mm: Int): String {
    var monthString = ""
    when (mm) {
        0 -> monthString = "Jan"
        1 -> monthString = "Feb"
        2 -> monthString = "Mar"
        3 -> monthString = "Apr"
        4 -> monthString = "Mei"
        5 -> monthString = "Jun"
        6 -> monthString = "Jul"
        7 -> monthString = "Ags"
        8 -> monthString = "Sep"
        9 -> monthString = "Okt"
        10 -> monthString = "Nov"
        11 -> monthString = "Des"
    }
    return monthString
}

// convert from initDateFormat to endDateFormat
@SuppressLint("SimpleDateFormat")
fun convertFormatDate(date: String, initDateFormat: String, endDateFormat: String): String {

    val initDate = SimpleDateFormat(initDateFormat).parse(date)
    val formatter = SimpleDateFormat(endDateFormat)

    return formatter.format(initDate)
}

