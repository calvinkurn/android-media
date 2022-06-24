package com.tokopedia.shop.common.data.model

data class ShopPageAtcTracker(
    val cartId: String,
    val productId: String,
    val productName: String,
    val productPrice: String,
    val isVariant: Boolean,
    val quantity: Int,
    val atcType: AtcType,
    val componentName: String
) {
    enum class AtcType {
        ADD,
        UPDATE_ADD,
        UPDATE_REMOVE,
        REMOVE
    }
}