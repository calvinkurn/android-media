package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetFlashSaleSellerStatusRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader
)