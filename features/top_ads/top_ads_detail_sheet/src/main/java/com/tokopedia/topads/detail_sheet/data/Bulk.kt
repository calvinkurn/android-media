package com.tokopedia.topads.detail_sheet.data


import com.google.gson.annotations.SerializedName

data class Bulk(
    @SerializedName("action")
    val action: String,
    @SerializedName("ads")
    val ads: List<Ad>,
    @SerializedName("shop_id")
    val shopId: String
)