package com.tokopedia.campaign.utils.extension

import com.tokopedia.campaign.utils.constant.LocaleConstant
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Date
import java.util.Locale

private const val DECIMAL_FORMAT_PATTERN = "#,###,###"

/**
 * Convert millis to Date object
 */
fun Long.toDate(): Date {
    return Date(this)
}

/**
 * Input: 1_000_000
 * Output: Rp1.000.000 (if locale is Indonesia)
 */
fun Long.splitByThousand(
    desiredOutputFormat: String = DECIMAL_FORMAT_PATTERN,
    locale: Locale = LocaleConstant.INDONESIA
): String {
    val symbol = DecimalFormatSymbols(locale)
    val formatter = DecimalFormat(desiredOutputFormat, symbol)
    return formatter.format(this)
}