package com.tokopedia.shop.flashsale.common.extension

import java.text.NumberFormat
import java.util.*

fun Int.isScrollUp() : Boolean {
    return this <= 0
}

fun Int.convertRupiah(): String {
    val localId = Locale("in", "ID")
    val formatter = NumberFormat.getCurrencyInstance(localId)
    return formatter.format(this)
}