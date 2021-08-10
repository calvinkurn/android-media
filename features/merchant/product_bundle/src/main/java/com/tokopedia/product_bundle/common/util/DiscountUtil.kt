package com.tokopedia.product_bundle.common.util

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

object DiscountUtil {
    fun getDiscountPercentage(originalPrice: Double, discountPrice: Double): Int {
        if (originalPrice <= 0) {
            return 0
        }
        val discPercentage = ((originalPrice - discountPrice) / originalPrice) * 100
        if (discPercentage > 0 && discPercentage < 1) {
            return ceil(discPercentage).toInt()
        }
        if (discPercentage > 99 && discPercentage < 100) {
            return floor(discPercentage).toInt()
        }
        return round(((originalPrice - discountPrice) / originalPrice) * 100).toInt()
    }
}