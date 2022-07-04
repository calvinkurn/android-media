package com.tokopedia.shop.flashsale.common.util

import com.tokopedia.kotlin.extensions.view.orZero
import kotlin.math.ceil

object DiscountUtil {

    private const val DISCOUNT_MAX = 100

    fun getDiscountPercent(originalPrice: Double, discountedPrice: Double): Double {
        return (DISCOUNT_MAX - (originalPrice / discountedPrice) * DISCOUNT_MAX)
    }

    fun getDiscountPercent(originalPrice: Long, discountedPrice: Long?): Long {
        val amount = getDiscountPercent(originalPrice.toDouble(), discountedPrice.orZero().toDouble())
        return ceil(amount).toLong()
    }

    fun getDiscountPrice(discountAmount: Double, originalPrice: Long): Double {
        val discountAmountInversed = (DISCOUNT_MAX - discountAmount) / DISCOUNT_MAX
        return  originalPrice * discountAmountInversed
    }

    fun getDiscountPrice(discountAmount: Long, originalPrice: Long?): Long {
        val discountedPrice = getDiscountPrice(discountAmount.toDouble(), originalPrice.orZero())
        return ceil(discountedPrice).toLong()
    }

    fun getPercentLong(percent: Double): Long {
        return ceil(DISCOUNT_MAX * percent).toLong()
    }

}