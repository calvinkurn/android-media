package com.tokopedia.tkpd.flashsale.common.extension

import com.tokopedia.campaign.utils.constant.LocaleConstant
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private const val EPOCH_TO_MILLIS_MULTIPLIER = 1000
const val DECIMAL_FORMAT_PATTERN = "#,###,###"

fun Long.toDate(): Date {
    return Date(this)
}


fun Long.isZero(): Boolean {
    return this == 0L
}

fun Long.epochToDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)
    return calendar.time
}

fun Long.splitByThousand() : String {
    val symbol = DecimalFormatSymbols(LocaleConstant.INDONESIA)
    val formatter = DecimalFormat(DECIMAL_FORMAT_PATTERN, symbol)
    return formatter.format(this)
}
