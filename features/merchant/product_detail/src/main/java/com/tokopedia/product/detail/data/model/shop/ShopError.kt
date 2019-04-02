package com.tokopedia.product.detail.data.model.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopError(
        @SerializedName("message")
        @Expose
        val message: String = ""
)