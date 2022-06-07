package com.tokopedia.shop.flashsale.domain.entity

import java.util.*

data class CampaignBanner(
    val campaignId: Long,
    val campaignName: String,
    val products: List<Product>,
    val maxDiscountPercentage: Int,
    val campaignStatusId: Int,
    val shop: Shop,
    val startDate: Date,
    val endDate: Date
) {
    data class Product(
        val imageUrl: String,
        val originalPrice: String,
        val discountedPrice: String,
        val discountPercentage: Int
    )

    data class Shop(
        val name: String,
        val domain: String,
        val logo: String,
        val isGold: Boolean,
        val isOfficial: Boolean,
        val badgeImageUrl: String
    )
}
