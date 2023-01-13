package com.tokopedia.buyerorderdetail.presentation.model

data class PofProductUnfulfilledUiModel(
    val productPictureUrl: String,
    val productName: String,
    val productPrice: String,
    val productQtyRequest: Long,
    val productQtyCheckout: Long
) {
    fun hasQtyGreenColor() = productQtyRequest > MINIMUM_PRODUCT_UNFULFILLED

    fun getProductPriceQty() = "$productQtyRequest x $productPrice"

    companion object {
        const val MINIMUM_PRODUCT_UNFULFILLED = 1L
    }
}
