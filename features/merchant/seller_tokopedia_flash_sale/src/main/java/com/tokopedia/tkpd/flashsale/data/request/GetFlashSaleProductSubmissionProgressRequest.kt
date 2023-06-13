package com.tokopedia.tkpd.flashsale.data.request


import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductSubmissionProgressRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader = CampaignParticipationRequestHeader(),
    @SerializedName("campaign_id")
    val campaignId: Long = 0L,
    @SerializedName("rows")
    val rows: Int = 0,
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("check_progress")
    val checkProgress: Boolean = false
)
