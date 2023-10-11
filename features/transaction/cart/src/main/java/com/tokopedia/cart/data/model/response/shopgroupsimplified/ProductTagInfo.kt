package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ProductTagInfo(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = ""
)
