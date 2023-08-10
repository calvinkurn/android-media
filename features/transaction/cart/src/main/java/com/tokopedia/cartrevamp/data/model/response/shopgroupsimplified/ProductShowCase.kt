package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ProductShowCase(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("id")
    val id: String = ""
)
