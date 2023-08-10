package com.tokopedia.content.common.util

/**
 * Created By : Jonathan Darwin on March 27, 2023
 */

fun Double.toPercent(): Int {
    return (this * PERCENTAGE_MULTIPLIER).toInt()
}

fun Int.toUnitInterval(): Double {
    return this / PERCENTAGE_MULTIPLIER.toDouble()
}

private const val PERCENTAGE_MULTIPLIER = 100
