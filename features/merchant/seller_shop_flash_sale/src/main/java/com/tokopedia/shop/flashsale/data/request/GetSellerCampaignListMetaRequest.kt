package com.tokopedia.shop.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetSellerCampaignListMetaRequest(
    @SerializedName("seller_campaign_type")
    val sellerCampaignType: Int
)