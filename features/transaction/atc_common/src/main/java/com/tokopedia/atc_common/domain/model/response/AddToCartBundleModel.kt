package com.tokopedia.atc_common.domain.model.response

data class AddToCartBundleModel(
        var success: Boolean = false,
        var errorMessage: String = "",
        var addToCartBundleDataModel: AddToCartBundleDataModel = AddToCartBundleDataModel()
)

data class AddToCartBundleDataModel(
        var message: List<String> = emptyList(),
        var data: List<ProductDataModel> = emptyList()
)

data class ProductDataModel(
        var customerId: String = "",
        var notes: String = "",
        var productId: String = "",
        var quantity: Int = 0,
        var shopId: String = "",
        var warehouseId: String = ""
)