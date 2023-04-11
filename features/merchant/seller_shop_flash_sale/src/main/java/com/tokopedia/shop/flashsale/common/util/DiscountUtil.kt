package com.tokopedia.shop.flashsale.common.util

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import kotlin.math.ceil

object DiscountUtil {

    private const val DISCOUNT_MAX = 100
    private const val DISCOUNT_INPUT_MAX = 99L
    private const val MAX_CAMPAIGN_DISCOUNT_PERCENTAGE = 0.99
    private const val MIN_CAMPAIGN_DISCOUNT_PERCENTAGE = 0.01

    fun getDiscountPercent(originalPrice: Double, discountedPrice: Double): Double {
        return (DISCOUNT_MAX - (originalPrice / discountedPrice) * DISCOUNT_MAX)
    }

    fun getDiscountPercent(originalPrice: Long, discountedPrice: Int?): Long {
        val amount = getDiscountPercent(originalPrice.toDouble(), discountedPrice.orZero().toDouble())
        return ceil(amount).toLong()
    }

    fun getDiscountPercentThresholded(originalPrice: Long, discountedPrice: Int?): Long {
        val finalDiscountPrice = getDiscountPercent(originalPrice, discountedPrice)
        return when {
            finalDiscountPrice.isLessThanZero() -> Int.ZERO.toLong()
            finalDiscountPrice > DISCOUNT_INPUT_MAX -> DISCOUNT_INPUT_MAX
            else -> finalDiscountPrice
        }
    }

    fun getDiscountPrice(discountAmount: Double, originalPrice: Int): Double {
        val discountAmountInversed = (DISCOUNT_MAX - discountAmount) / DISCOUNT_MAX
        return originalPrice * discountAmountInversed
    }

    fun getDiscountPrice(discountAmount: Long, originalPrice: Int?): Long {
        val discountedPrice = getDiscountPrice(discountAmount.toDouble(), originalPrice.orZero())
        return ceil(discountedPrice).toLong()
    }

    fun getPercentLong(percent: Double): Long {
        return ceil(DISCOUNT_MAX * percent).toLong()
    }

    fun getProductMaxDiscountedPrice(originalPrice: Long): Int {
        return ceil(originalPrice * MAX_CAMPAIGN_DISCOUNT_PERCENTAGE).toInt()
    }

    fun getProductMinDiscountedPrice(originalPrice: Long): Int {
        return ceil(originalPrice * MIN_CAMPAIGN_DISCOUNT_PERCENTAGE).toInt()
    }

}
