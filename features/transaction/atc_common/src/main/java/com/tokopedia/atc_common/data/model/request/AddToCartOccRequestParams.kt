package com.tokopedia.atc_common.data.model.request

data class AddToCartOccRequestParams(
        var productId: String,
        var shopId: String,
        var quantity: String,
        var warehouseId: String = "",
        var lang: String = "id",
        var isScp: Boolean = false,
        var ucParam: String = "",
        var attribution: String = "",
        var listTracker: String = "",
        var notes: String = ""
)