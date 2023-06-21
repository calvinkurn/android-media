package com.tokopedia.shopdiscount.bulk.domain.entity

import java.util.Date

data class DiscountSettings(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val discountType: DiscountType,
    val discountAmount: Int,
    val maxPurchaseQuantity: Int,
    val isUsingCustomPeriod: Boolean
)
