package com.tokopedia.home.account.analytics.data.model


import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("shop_name")
    val shopName: String = ""
)