package com.tokopedia.kotlin.extensions.view

import java.util.Locale

private const val MEGA_BYTE_SIZE = 1000000L

fun Long?.formattedToMB(): String {
    if (this == 0L || this == null) return "0"
    val sizeFormat = "%.2f"

    try {
        val sizeInMB = this.toFloat().div(MEGA_BYTE_SIZE)
        return String.format(Locale.ENGLISH, sizeFormat, sizeInMB)
    } catch (e: Exception) {
        return "0"
    }
}