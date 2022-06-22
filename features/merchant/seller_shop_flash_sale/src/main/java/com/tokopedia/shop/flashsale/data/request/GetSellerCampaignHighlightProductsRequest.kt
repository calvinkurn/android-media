package com.tokopedia.shop.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetSellerCampaignHighlightProductsRequest(
    @SuppressLint("Invalid Data Type")
    @SerializedName("campaign_id")
    val campaignId: Long
)
