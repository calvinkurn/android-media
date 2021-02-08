package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName

data class Wholesale(
        @SerializedName("price")
        val price: WholesalePrice = WholesalePrice(),

        @SerializedName("minQty")
        val minQty: Int = 0
)

data class WholesalePrice(
        @SerializedName("value")
        val value: Int = 0
)