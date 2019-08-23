package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopError(
        @SerializedName("message")
        @Expose
        val message: String = ""
)