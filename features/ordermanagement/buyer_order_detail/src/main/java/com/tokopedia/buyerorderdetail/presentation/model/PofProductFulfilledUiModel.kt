package com.tokopedia.buyerorderdetail.presentation.model

data class PofProductFulfilledUiModel(
    val productPictureUrl: String,
    val productName: String,
    val productPrice: String,
    val productQty: Long,
) {
    fun getProductPriceQty() = "$productQty x $productPrice"
}
