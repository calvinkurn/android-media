package com.tokopedia.shop.info.domain.entity

data class ShopPerformance(
    val totalProductSoldCount: String,
    val chatPerformance: ShopPerformanceDuration,
    val orderProcessTime: String
)
