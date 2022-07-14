package com.tokopedia.campaign.utils.extension

import com.tokopedia.campaign.utils.constant.LocaleConstant
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Date

const val DECIMAL_FORMAT_PATTERN = "#,###,###"

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.splitByThousand() : String {
    val symbol = DecimalFormatSymbols(LocaleConstant.INDONESIA)
    val formatter = DecimalFormat(DECIMAL_FORMAT_PATTERN, symbol)
    return formatter.format(this)
}