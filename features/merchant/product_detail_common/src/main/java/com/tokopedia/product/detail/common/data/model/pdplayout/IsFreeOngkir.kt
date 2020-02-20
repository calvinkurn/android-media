package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class IsFreeOngkir(
    @SerializedName("imageURL")
    val imageURL: String = "",
    @SerializedName("isActive")
    val isActive: Boolean = false
)