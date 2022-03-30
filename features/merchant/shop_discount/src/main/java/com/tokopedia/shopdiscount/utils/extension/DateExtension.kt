package com.tokopedia.shopdiscount.utils.extension

import com.tokopedia.shopdiscount.utils.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*

private const val EMPTY_STRING = ""

fun Date.parseTo(desiredOutputFormat: String, locale: Locale = LocaleConstant.INDONESIA): String {
    return try {
        val outputFormat = SimpleDateFormat(desiredOutputFormat, locale)
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
