package com.tokopedia.shop.flash_sale.common.extension

import com.tokopedia.shop.flash_sale.common.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*

private const val MILLIS_TO_EPOCH_MULTIPLIER = 1000

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.epochToDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.time
}

fun Long.epochTo(desiredOutputFormat : String): String {
    val dateFormat = SimpleDateFormat(desiredOutputFormat, LocaleConstant.INDONESIA)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val date = Date(this * MILLIS_TO_EPOCH_MULTIPLIER)

    return dateFormat.format(date)
}