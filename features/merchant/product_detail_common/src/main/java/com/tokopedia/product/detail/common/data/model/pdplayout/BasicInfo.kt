package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class BasicInfo(
    @SerializedName("CategoryID")
    val categoryID: String = "",
    @SerializedName("ProductID")
    val productID: String = ""
)