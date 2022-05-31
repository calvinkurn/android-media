package com.tokopedia.shop.flash_sale.common.extension

import com.tokopedia.shop.flash_sale.common.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*

private const val EPOCH_TO_MILLIS_MULTIPLIER = 1000
private const val OFFSET_WIB = 7

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.epochToDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val decreased = hour - OFFSET_WIB
    calendar.set(Calendar.HOUR_OF_DAY, decreased)
    return calendar.time
}

fun Long.epochTo(desiredOutputFormat: String): String {
    val dateFormat = SimpleDateFormat(desiredOutputFormat, LocaleConstant.INDONESIA)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val date = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)

    return dateFormat.format(date)
}