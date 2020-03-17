package com.tokopedia.product.detail.common.data.model.carttype


import com.google.gson.annotations.SerializedName

data class CartRedirectionParams(
    @SerializedName("campaign_id")
    val campaignId: Int = 0,
    @SerializedName("campaign_type_id")
    val campaignTypeId: Int = 0,
    @SerializedName("flags")
    val flags: List<String> = listOf()
)