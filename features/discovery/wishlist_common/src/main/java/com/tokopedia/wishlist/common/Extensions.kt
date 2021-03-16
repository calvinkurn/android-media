package com.tokopedia.wishlist.common

fun String.toEmptyStringIfZero(): String {
    val intValue = toIntOrNull()
    if (intValue == null || intValue <= 0) {
        return ""
    }
    return this
}