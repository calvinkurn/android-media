package com.tokopedia.shop.flashsale.common.extension

import java.text.NumberFormat
import java.util.*

fun Int.isScrollUp() : Boolean {
    return this <= 0
}

fun Number.convertRupiah(): String {
    val localId = Locale("id", "ID")
    val formatter = NumberFormat.getCurrencyInstance(localId)
    formatter.maximumFractionDigits = 0
    return formatter.format(this)
}