package com.tokopedia.campaign.utils.extension

import com.tokopedia.campaign.utils.constant.LocaleConstant
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private const val DECIMAL_FORMAT_PATTERN = "#,###,###"

/**
 * Input: 1_000_000
 * Output: 1.000.000 (if locale is Indonesia)
 */
fun Number.splitByThousand(
    desiredOutputFormat: String = DECIMAL_FORMAT_PATTERN,
    locale: Locale = LocaleConstant.INDONESIA
): String {
    val symbol = DecimalFormatSymbols(locale)
    val formatter = DecimalFormat(desiredOutputFormat, symbol)
    return formatter.format(this)
}