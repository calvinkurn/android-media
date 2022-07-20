package com.tokopedia.shop_widget.mvc_locked_to_product.analytic.model

data class MvcLockedToProductAddToCartTracker(
    val cartId: String,
    val productId: String,
    val quantity: Int,
    val atcType: AtcType
) {
    enum class AtcType {
        ADD,
        UPDATE_ADD,
        UPDATE_REMOVE,
        REMOVE
    }
}