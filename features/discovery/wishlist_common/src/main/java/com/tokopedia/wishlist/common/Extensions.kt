package com.tokopedia.wishlist.common

fun String.toEmptyStringIfZero(): String {
    val longValue = toLongOrNull()
    if (longValue == null || longValue <= 0L) {
        return ""
    }
    return this
}