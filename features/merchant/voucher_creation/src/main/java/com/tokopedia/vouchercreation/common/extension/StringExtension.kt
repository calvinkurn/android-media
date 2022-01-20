package com.tokopedia.vouchercreation.common.extension

fun String.digitsOnlyInt() : Int {
    return try {
        this.filter { it.isDigit() }.toInt()
    } catch (e: Exception) {
        0
    }
}