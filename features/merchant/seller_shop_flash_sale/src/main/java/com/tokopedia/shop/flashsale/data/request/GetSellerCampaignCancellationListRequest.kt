package com.tokopedia.shop.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetSellerCampaignCancellationListRequest(
    @SerializedName("campaign_type")
    val sellerCampaignType: Int
)
