package com.tokopedia.shop.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetSellerCampaignEligibilityRequest(
    @SerializedName("campaign_type")
    val campaignType: Int
)