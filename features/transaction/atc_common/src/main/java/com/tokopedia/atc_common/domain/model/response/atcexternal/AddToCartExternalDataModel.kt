package com.tokopedia.atc_common.domain.model.response.atcexternal

data class AddToCartExternalDataModel(
        var message: List<String> = emptyList(),
        var success: Int = 0,
        var data: DataAddToCartModel = DataAddToCartModel()
)

data class DataAddToCartModel(
        var productId: Int = 0,
        var productName: String = "",
        var quantity: Int = 0,
        var price: Int = 0,
        var category: String = "",
        var shopId: Int = 0,
        var shopType: String = "",
        var shopName: String = "",
        var picture: String = "",
        var url: String = "",
        var cartId: Int = 0,
        var brand: String = "",
        var categoryId: String = "",
        var variant: String = "",
        var trackerAttribution: String = "",
        var isMultiOrigin: Boolean = false,
        var isFreeOngkir: Boolean = false
)