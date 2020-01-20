package com.tokopedia.tkpd.tkpdreputation.createreputation.model


import com.google.gson.annotations.SerializedName

data class ShopData(
    @SerializedName("shopID")
    val shopID: Int = 0,
    @SerializedName("shopOpen")
    val shopOpen: Boolean = false
)