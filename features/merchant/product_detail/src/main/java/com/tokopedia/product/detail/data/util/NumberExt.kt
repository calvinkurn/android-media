package com.tokopedia.product.detail.data.util

import java.text.NumberFormat
import java.util.*

val IDRLocale = NumberFormat.getCurrencyInstance(Locale("in", "id"))

fun Int.getCurrencyFormatted(): String {
    IDRLocale.maximumFractionDigits = 0
    return IDRLocale.format(this)
}