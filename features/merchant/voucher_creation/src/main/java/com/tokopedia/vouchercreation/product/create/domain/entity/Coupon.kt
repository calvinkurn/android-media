package com.tokopedia.vouchercreation.product.create.domain.entity

data class Coupon(
    val type: CouponType,
    val discountType: DiscountType,
    val minimumPurchaseType: MinimumPurchaseType,
    val discountAmount: Int,
    val discountPercentage: Int,
    val maxDiscount: Int,
    val quota: Int,
    val minimumPurchase: Int,
    val estimatedMaxExpense: Long
)