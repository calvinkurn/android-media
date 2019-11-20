package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class BasicInfo(
    @SerializedName("categoryID")
    val categoryID: String = "",
    @SerializedName("productID")
    val productID: String = ""
)