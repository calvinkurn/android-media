package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetFlashSaleListForSellerCategoryRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
    @SerializedName("tab_name")
    val tabName: String
)
