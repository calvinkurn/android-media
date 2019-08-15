package com.tokopedia.product.manage.list.data.model.productlist


import com.google.gson.annotations.SerializedName

data class GetProductList(
        @SerializedName("data")
        val data: List<Data> = listOf(),
        @SerializedName("errors")
        val errors: String = "",
        @SerializedName("links")
        val links: Links = Links(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("totalData")
        val totalData: Int = 0
)