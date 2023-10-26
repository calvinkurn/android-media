package com.tokopedia.shop.info.domain.entity

data class ShopEpharmacyInfo(
    val nearestPickupAddress: String,
    val nearPickupAddressAppLink: String,
    val pharmacistOperationalHour: String,
    val pharmacistName: String, 
    val siaNumber: String
)
