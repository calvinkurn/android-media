package com.tokopedia.search.result.product.addtocart

data class AddToCartData(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val priceStr: String,
    val type: String,
    val shopId: String,
    val parentId: String,
    val componentId: String,
    val topAdsClickUrl: String,
    val topAdsImpressionUrl: String,
    val imageUrl: String,
) {

    fun shouldOpenVariantBottomSheet(): Boolean {
        return parentId != "" && parentId != "0"
    }
}
