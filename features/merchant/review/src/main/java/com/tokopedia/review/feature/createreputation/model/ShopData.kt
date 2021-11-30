package com.tokopedia.review.feature.createreputation.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopData(
    @SerializedName("shopIDStr")
    @Expose
    val shopIDStr: String = "",
    @SerializedName("shopOpen")
    @Expose
    val shopOpen: Boolean = false,
    @SerializedName("shopName")
    @Expose
    val shopName: String = ""
)