package com.tokopedia.atc_common.domain.model.response.atcexternal

data class AddToCartExternalModel(
    var message: List<String> = emptyList(),
    var success: Int = 0,
    var data: AddToCartExternalDataModel = AddToCartExternalDataModel()
)

data class AddToCartExternalDataModel(
    var productId: String = "",
    var productName: String = "",
    var quantity: Int = 0,
    var price: Double = 0.0,
    var category: String = "",
    var shopId: String = "",
    var shopType: String = "",
    var shopName: String = "",
    var picture: String = "",
    var url: String = "",
    var cartId: String = "",
    var brand: String = "",
    var categoryId: String = "",
    var variant: String = "",
    var trackerAttribution: String = "",
    var isMultiOrigin: Boolean = false,
    var isFreeOngkir: Boolean = false,
    var isFreeOngkirExtra: Boolean = false
)
