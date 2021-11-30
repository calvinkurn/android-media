package com.tokopedia.product.detail.data.util

import kotlin.math.roundToInt

/**
 * This Extension Function wrap [roundToInt]
 * if throw will return 0
 */
fun Double.roundToIntOrZero(): Int {
    return try {
        roundToInt()
    } catch (ex: IllegalArgumentException) {
        0
    }
}