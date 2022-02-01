package com.tokopedia.vouchercreation.product.create.domain.entity

import java.io.Serializable

data class CouponSettings(
    val type: CouponType,
    val discountType: DiscountType,
    val minimumPurchaseType: MinimumPurchaseType,
    val discountAmount: Int,
    val discountPercentage: Int,
    val maxDiscount: Int,
    val quota: Int,
    val minimumPurchase: Int,
    val estimatedMaxExpense: Long
) : Serializable