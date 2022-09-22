package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class DoFlashSaleSellerRegistrationRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
    @SerializedName("campaign_id")
    val campaignId: Long
)
