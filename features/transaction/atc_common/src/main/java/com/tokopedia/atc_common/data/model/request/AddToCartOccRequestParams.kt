package com.tokopedia.atc_common.data.model.request

import com.google.gson.annotations.SerializedName

data class AddToCartOccRequestParams(
        var productId: Long,
        var shopId: Int,
        var quantity: Int,
        var warehouseId: Int = 0,
        var lang: String = "",
        var isScp: Boolean = false,
        var ucParam: String = "",
        var attribution: String = "",
        var listTracker: String = ""
)