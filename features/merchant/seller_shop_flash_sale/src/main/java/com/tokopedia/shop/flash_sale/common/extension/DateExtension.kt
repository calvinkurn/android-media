package com.tokopedia.shop.flash_sale.common.extension

import com.tokopedia.shop.flash_sale.common.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*

private const val EMPTY_STRING = ""

fun Date.formatTo(desiredOutputFormat: String, locale: Locale = LocaleConstant.INDONESIA): String {
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

fun Long.unixToMs(): Long {
    return this * 1000L
}