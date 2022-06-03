package com.tokopedia.shop.flash_sale.data.response

import com.google.gson.annotations.SerializedName

data class GetSellerCampaignCancellationListResponse(
    @SerializedName("getSellerCampaignCancellationList")
    val cancellationList: CancellationReason = CancellationReason()
)

data class CancellationReason (
    @SerializedName("cancellation_reason")
    val cancellationList: List<String> = emptyList()
)
