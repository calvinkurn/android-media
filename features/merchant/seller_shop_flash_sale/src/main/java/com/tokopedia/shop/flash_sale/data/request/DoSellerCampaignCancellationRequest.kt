package com.tokopedia.shop.flash_sale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class DoSellerCampaignCancellationRequest(
    @SuppressLint("Invalid Data Type") // BE still using number
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("cancellation_reason")
    val cancellationReason: String
)
