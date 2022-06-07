package com.tokopedia.shop.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetSellerCampaignAttributeRequest(
    @SerializedName("seller_campaign_type")
    val sellerCampaignType: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("year")
    val year: Int,
    @SerializedName("field")
    val field: Field
) {
    data class Field(
        @SerializedName("shop_attribute") val shopAttribute: Boolean,
        @SerializedName("campaign_detail") val campaignDetail: Boolean
    )
}