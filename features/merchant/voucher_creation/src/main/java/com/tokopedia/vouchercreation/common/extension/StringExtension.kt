package com.tokopedia.vouchercreation.common.extension

import com.tokopedia.kotlin.extensions.view.toIntOrZero

fun String.digitsOnlyInt() : Int {
    return try {
        this.filter { it.isDigit() }.toIntOrZero()
    } catch (e: Exception) {
        0
    }
}
