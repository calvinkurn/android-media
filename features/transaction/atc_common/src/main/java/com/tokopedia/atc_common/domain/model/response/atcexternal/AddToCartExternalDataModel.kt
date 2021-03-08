package com.tokopedia.atc_common.domain.model.response.atcexternal

data class AddToCartExternalModel(
        var message: List<String> = emptyList(),
        var success: Int = 0,
        var data: AddToCartExternalDataModel = AddToCartExternalDataModel()
)

data class AddToCartExternalDataModel(
        var productId: Long = 0,
        var productName: String = "",
        var quantity: Int = 0,
        var price: Int = 0,
        var category: String = "",
        var shopId: Long = 0,
        var shopType: String = "",
        var shopName: String = "",
        var picture: String = "",
        var url: String = "",
        var cartId: Long = 0,
        var brand: String = "",
        var categoryId: String = "",
        var variant: String = "",
        var trackerAttribution: String = "",
        var isMultiOrigin: Boolean = false,
        var isFreeOngkir: Boolean = false,
        var isFreeOngkirExtra: Boolean = false
)