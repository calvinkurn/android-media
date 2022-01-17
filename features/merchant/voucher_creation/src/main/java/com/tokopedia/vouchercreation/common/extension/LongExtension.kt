package com.tokopedia.vouchercreation.common.extension

import com.tokopedia.vouchercreation.common.consts.LocaleConstant
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun Long.splitByThousand() : String {
    val symbol = DecimalFormatSymbols(LocaleConstant.INDONESIA)
    val formatter = DecimalFormat("#,###,###", symbol)
    return formatter.format(this)
}