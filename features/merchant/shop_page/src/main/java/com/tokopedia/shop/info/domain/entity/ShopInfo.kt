package com.tokopedia.shop.info.domain.entity

data class ShopInfo(
    val shopImageUrl: String,
    val shopBadgeUrl: String,
    val shopName: String,
    val shopDescription: String,
    val mainLocation: String,
    val operationalHours: Map<String, List<ShopOperationalHourWithStatus>>,
    val shopJoinDate: String,
    val totalProduct: Int,
    val shopUsp: List<String>,
    val showPharmacyLicenseBadge: Boolean
)
