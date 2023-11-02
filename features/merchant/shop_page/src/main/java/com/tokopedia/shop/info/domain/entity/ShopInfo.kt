package com.tokopedia.shop.info.domain.entity

data class ShopInfo(
    val shopImageUrl: String,
    val shopBadgeUrl: String,
    val shopName: String,
    val shopDescription: String,
    val mainLocation: String,
    val otherLocations: List<String>,
    val operationalHours: Map<String, List<String>>,
    val shopJoinDate: String,
    val totalProduct: Int
)
