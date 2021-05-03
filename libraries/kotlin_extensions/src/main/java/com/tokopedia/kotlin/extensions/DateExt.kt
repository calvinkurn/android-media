package com.tokopedia.kotlin.extensions

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import kotlin.math.roundToInt


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

val Date.relativeDate: String
    get() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val dateActive = Calendar.getInstance(Locale.getDefault())
        dateActive.timeInMillis = time
        val stringToday = "Hari ini"
        val stringDaysAgo = "hari lalu"
        val stringWeeksAgo = "minggu lalu"
        val stringMonthsAgo = "bulan lalu"
        val stringOverOneYearAgo = "Lebih dari 1 tahun lalu"
        val timeOffset = System.currentTimeMillis() - dateActive.timeInMillis

        val days = (timeOffset / (24.0 * 60 * 60 * 1000)).roundToInt()

        return when {
            DateUtils.isToday(time) -> stringToday
            days in 1..6 -> "$days $stringDaysAgo"
            days < 30 -> {
                val weeks = (days / 7)
                "$weeks $stringWeeksAgo"
            }
            days < 365 -> {
                val month = (days / 30)
                "$month $stringMonthsAgo"
            }
            days > 365 -> {
                stringOverOneYearAgo
            }
            else -> dateFormat.format(this)
        }
    }


fun Date.toFormattedString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

// return n days interval in formatted
fun getCalculatedFormattedDate(format: String, n: Int): CharSequence {
    val dateParam = GregorianCalendar()
    dateParam.add(DAY_OF_MONTH, n)
    return DateFormat.format(format, dateParam.time)
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

fun String.convertToDate(format: String, locale: Locale = Locale.getDefault()): Date {
    val formatter = SimpleDateFormat(format, locale)
    return try {
        formatter.parse(this)
    } catch (ex: ParseException) {
        ex.printStackTrace()
        throw RuntimeException(ex.message)
    }
}

