package com.tokopedia.vouchercreation.common.extension

import com.tokopedia.vouchercreation.common.consts.LocaleConstant
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

const val DECIMAL_FORMAT_PATTERN = "#,###,###"

fun Int.splitByThousand() : String {
    val symbol = DecimalFormatSymbols(LocaleConstant.INDONESIA)
    val formatter = DecimalFormat(DECIMAL_FORMAT_PATTERN, symbol)
    return formatter.format(this)
}