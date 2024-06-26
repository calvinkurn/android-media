package com.tokopedia.tkpd.flashsale.util.extension

import java.util.Date
import java.util.Calendar

private const val EPOCH_TO_MILLIS_MULTIPLIER = 1000

fun Long.epochToDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)
    return calendar.time
}
