package com.tokopedia.discovery2.analytics.merchantvoucher

data class MvcTrackingProperties(
    val name: String,
    val shopId: String,
    val creativeName: String,
    val position: Int,
    val action: String,
    val gtmItem: String,
    val tabName: String
)
