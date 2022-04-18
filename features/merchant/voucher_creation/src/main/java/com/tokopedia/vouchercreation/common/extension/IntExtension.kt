package com.tokopedia.vouchercreation.common.extension

import com.tokopedia.vouchercreation.common.consts.LocaleConstant
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

const val DECIMAL_FORMAT_PATTERN = "#,###,###"

fun Int.splitByThousand(locale : Locale = LocaleConstant.INDONESIA) : String {
    val symbol = DecimalFormatSymbols(locale)
    val formatter = DecimalFormat(DECIMAL_FORMAT_PATTERN, symbol)
    return formatter.format(this)
}