package com.tokopedia.atc_common.data.model.request

data class AddToCartOccMultiRequestParams(
        var carts: List<AddToCartOccMultiCartParam>,
        var lang: String = "id",
        var source: String = SOURCE_PDP,
) {
    companion object {
        const val SOURCE_PDP = "pdp"
        const val SOURCE_MINICART = "minicart"
    }
}

data class AddToCartOccMultiCartParam(
        var cartId: String? = null,
        var productId: String,
        var shopId: String,
        var quantity: String,
        var notes: String = "",
        var warehouseId: String = "0",
        var ucParam: String = "",
        var attribution: String = "",
        var listTracker: String = "",

        // analytics data
        var productName: String = "",
        var category: String = "",
        var price: String = "",
        var userId: String = "",
        var categoryLevel1Id: String = "",
        var categoryLevel1Name: String = "",
        var categoryLevel2Id: String = "",
        var categoryLevel2Name: String = "",
        var categoryLevel3Id: String = "",
        var categoryLevel3Name: String = ""
)