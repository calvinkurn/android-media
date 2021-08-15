package com.tokopedia.kotlin.extensions.view

import java.util.Locale

private const val MEGA_BYTE_SIZE = 1000000L

fun Long.formattedToMB(): String {
    val sizeFormat = "%.2f"

    val sizeInMB = this.toFloat() / MEGA_BYTE_SIZE
    return String.format(Locale.ENGLISH, sizeFormat, sizeInMB)
}