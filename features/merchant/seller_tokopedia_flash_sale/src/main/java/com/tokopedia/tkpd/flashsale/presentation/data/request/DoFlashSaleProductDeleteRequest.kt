package com.tokopedia.tkpd.flashsale.presentation.data.request

import com.google.gson.annotations.SerializedName

data class DoFlashSaleProductDeleteRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("product_ids")
    val productIds: List<Long>,
    @SerializedName("reservation_id")
    val reservationId: String
)