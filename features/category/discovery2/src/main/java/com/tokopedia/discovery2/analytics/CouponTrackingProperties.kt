package com.tokopedia.discovery2.analytics

data class CouponTrackingProperties(
    val componentName: String,
    val name: String,
    val id: String,
    val promoCode: String,
    val creativeName: String,
    val position: Int,
    val action: String,
    val gtmItem: String
)
