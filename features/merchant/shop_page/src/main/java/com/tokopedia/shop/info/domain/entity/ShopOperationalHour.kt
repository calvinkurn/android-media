package com.tokopedia.shop.info.domain.entity

data class ShopOperationalHour(
    val day: String,
    val startTime: String,
    val endTime: String,
    val status: Int
)
