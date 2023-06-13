package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductCriteriaCheckingRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("product_id")
    val productId: Long,
    @SerializedName("product_criteria_id")
    val productCriteriaId: Long,
)
