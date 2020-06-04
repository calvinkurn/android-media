package com.tokopedia.shop_showcase.common.util

import java.text.NumberFormat
import java.util.*

val IDRLocale: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "id"))

fun Number.getCurrencyFormatted(): String {
    IDRLocale.maximumFractionDigits = 0
    return IDRLocale.format(this)
}