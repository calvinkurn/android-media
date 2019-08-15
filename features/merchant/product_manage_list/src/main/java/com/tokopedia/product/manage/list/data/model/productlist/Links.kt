package com.tokopedia.product.manage.list.data.model.productlist


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("next")
    val next: String = "",
    @SerializedName("prev")
    val prev: String = "",
    @SerializedName("self")
    val self: String = ""
)