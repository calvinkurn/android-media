package com.tokopedia.shop.flashsale.domain.entity

data class HighlightedProduct(
    val campaignStatus: Int,
    val discountedPercentage: Int,
    val discountedPrice: String,
    val endDate: String,
    val id: Long,
    val imageUrl: String,
    val name: String,
    val originalPrice: String,
    val price: String,
    val startDate: String,
    val url: String
)