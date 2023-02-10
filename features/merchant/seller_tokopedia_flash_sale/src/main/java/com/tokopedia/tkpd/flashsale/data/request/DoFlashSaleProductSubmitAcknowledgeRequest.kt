package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class DoFlashSaleProductSubmitAcknowledgeRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader = CampaignParticipationRequestHeader(),
    @SerializedName("campaign_id")
    val campaignId: Long = 0L
)
