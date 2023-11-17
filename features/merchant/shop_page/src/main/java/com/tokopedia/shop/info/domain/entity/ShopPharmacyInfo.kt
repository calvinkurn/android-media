package com.tokopedia.shop.info.domain.entity

data class ShopPharmacyInfo(
    val showPharmacyInfoSection: Boolean,
    val nearestPickupAddress: String,
    val nearestPickupAddressGmapsUrl: String,
    val pharmacistOperationalHour: List<String>,
    val pharmacistName: String,
    val siaNumber: String,
    val sipaNumber: String,
    val expandPharmacyInfo: Boolean
)
