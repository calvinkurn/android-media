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

    /**
     * Enum for action activity
     * @property ADD for action adding item
     * @property UPDATE_ADD for action add quantity
     * @property UPDATE_REMOVE for action minus quantity
     * @property REMOVE for action removing item
     * */
    enum class AtcType {
        ADD,
        UPDATE_ADD,
        UPDATE_REMOVE,
        REMOVE
    }
}
