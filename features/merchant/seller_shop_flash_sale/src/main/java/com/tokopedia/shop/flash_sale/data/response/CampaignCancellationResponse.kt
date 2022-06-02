package com.tokopedia.shop.flash_sale.domain.entity.CampaignCancellation

import com.google.gson.annotations.SerializedName

data class CampaignCancellationResponse(
    @SerializedName("getSellerCampaignCancellationList")
    val cancellationList: CancellationReason = CancellationReason()
)

data class CancellationReason (
    @SerializedName("cancellation_reason")
    val cancellationList: List<String> = emptyList()
)
