package com.tokopedia.tokopedianow.common.model

data class NowAffiliateAtcData(
    val productId: String,
    val stock: Int,
    val isVariant: Boolean,
    val newQuantity: Int,
    val currentQuantity: Int
)
