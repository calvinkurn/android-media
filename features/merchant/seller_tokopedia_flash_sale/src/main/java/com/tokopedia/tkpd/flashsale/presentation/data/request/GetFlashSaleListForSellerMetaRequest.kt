package com.tokopedia.tkpd.flashsale.presentation.data.request

import com.google.gson.annotations.SerializedName

data class GetFlashSaleListForSellerMetaRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
) {
    data class CampaignParticipationRequestHeader(
        @SerializedName("source")
        val source: String = "fe",
        @SerializedName("ip")
        val ip: String = "",
        @SerializedName("usecase")
        val usecase: String
    )
}
