package com.tokopedia.shopdiscount.utils.extension

fun String.digitsOnly() : Int {
    return try {
        this.filter { it.isDigit() }.toInt()
    } catch (e: Exception) {
        0
    }
}