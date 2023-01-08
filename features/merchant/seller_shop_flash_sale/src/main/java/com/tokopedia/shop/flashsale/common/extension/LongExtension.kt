package com.tokopedia.shop.flashsale.common.extension

import java.util.Calendar
import java.util.Date

private const val EPOCH_TO_MILLIS_MULTIPLIER = 1000

fun Long.isZero(): Boolean {
    return this == 0L
}

fun Long.epochToDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)
    return calendar.time
}
