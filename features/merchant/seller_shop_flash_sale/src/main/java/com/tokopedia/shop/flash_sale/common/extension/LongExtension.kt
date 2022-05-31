package com.tokopedia.shop.flash_sale.common.extension

import com.tokopedia.shop.flash_sale.common.constant.DateConstant
import com.tokopedia.shop.flash_sale.common.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*

private const val EPOCH_TO_MILLIS_MULTIPLIER = 1000

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.epochToDate(): Calendar {
    val calendar = Calendar.getInstance()
    val date = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)
    calendar.time = date
    return calendar
}

fun Long.epochTo(desiredOutputFormat: String): String {
    val dateFormat = SimpleDateFormat(desiredOutputFormat, LocaleConstant.INDONESIA)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val date = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)

    return dateFormat.format(date)
}