package com.tokopedia.content.common.util

/**
 * Created By : Jonathan Darwin on March 27, 2023
 */

fun Double.toPercent(): Int {
    /**
     * result : range from 0 - 100
     */
    return (this * PERCENTAGE_MULTIPLIER).toInt()
}

fun Int.toFuzzyPercent(): Double {
    /**
     * result : range from 0.0 - 1.0
     */
    return this / PERCENTAGE_MULTIPLIER.toDouble()
}

private const val PERCENTAGE_MULTIPLIER = 100
