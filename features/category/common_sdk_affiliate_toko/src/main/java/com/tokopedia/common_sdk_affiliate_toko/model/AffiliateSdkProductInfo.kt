package com.tokopedia.common_sdk_affiliate_toko.model

data class AffiliateSdkProductInfo(
    val productId: String = "",
    val CategoryID: String = "",
    val IsVariant: Boolean = false,
    val StockQty: Int = 0
)