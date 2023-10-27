package com.tokopedia.shop.info.domain.entity

data class ShopOperationalHour(
    val day: Int,
    val startTime: String,
    val endTime: String,
    var status: Int
)
