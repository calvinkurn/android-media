package com.tokopedia.kotlin.extensions.view

import java.util.Locale

fun Long.formattedToMB(): String {
    val megaByteSize: Long = 1000000
    val sizeFormat = "%.2f"

    val sizeInMB = this.toFloat() / megaByteSize
    return String.format(Locale.ENGLISH, sizeFormat, sizeInMB)
}