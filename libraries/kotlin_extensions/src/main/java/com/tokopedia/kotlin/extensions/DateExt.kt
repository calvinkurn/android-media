package com.tokopedia.kotlin.extensions

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

fun getCalculatedFormattedDate(format: String, days: Int, locale: Locale = Locale.getDefault()): String {
    val cal = Calendar.getInstance()
    val formatter = SimpleDateFormat(format, locale)
    cal.add(Calendar.DAY_OF_YEAR, days)
    return formatter.format(Date(cal.timeInMillis))
}

