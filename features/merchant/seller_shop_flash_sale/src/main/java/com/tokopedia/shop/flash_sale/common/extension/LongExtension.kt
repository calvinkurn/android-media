package com.tokopedia.shop.flash_sale.common.extension

import java.util.*

private const val EPOCH_TO_MILLIS_MULTIPLIER = 1000

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.epochToDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)
    calendar.timeZone = TimeZone.getTimeZone("GMT")
    return calendar.time
}
