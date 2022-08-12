package com.tokopedia.tkpd.flashsale.presentation.data.request

import com.google.gson.annotations.SerializedName

data class GetFlashSaleListForSellerMetaRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
)