package com.tokopedia.review.feature.createreputation.model


import com.google.gson.annotations.SerializedName

data class ShopData(
    @SerializedName("shopID")
    val shopID: Int = 0,
    @SerializedName("shopOpen")
    val shopOpen: Boolean = false,
    @SerializedName("shopName")
    val shopName: String = ""
)