package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Wholesale(
        @SerializedName("minQty")
        val minQty: Int = 0,
        @SerializedName("price")
        val price: String = ""
)