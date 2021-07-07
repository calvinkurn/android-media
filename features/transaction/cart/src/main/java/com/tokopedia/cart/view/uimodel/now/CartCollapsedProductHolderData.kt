package com.tokopedia.cart.view.uimodel.now

data class CartCollapsedProductHolderData(
        var productId: String = "",
        var productImageUrl: String = "",
        var productName: String = "",
        var productPrice: Long = 0L,
        var productVariantName: String = "",
        var productQuantity: Int = 0
)