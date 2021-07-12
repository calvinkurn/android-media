package com.tokopedia.purchase_platform.common.feature.sellercashback

data class ShipmentSellerCashbackModel(
        var sellerCashback: Int = 0,
        var sellerCashbackFmt: String? = null,
        var isVisible: Boolean = false
)