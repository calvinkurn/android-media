package com.tokopedia.shop.common.data.model

data class ShopPageProductDirectPurchaseWidgetAtcTracker(
    val totalEtalaseGroup: Int,
    val etalaseId: String,
    val cartId: String,
    val productId: String,
    val productName: String,
    val productPrice: String,
    val isVariant: Boolean,
    val minOrder: Int
)
