package com.tokopedia.shop.flash_sale.data.request

import com.google.gson.annotations.SerializedName

data class GetSellerCampaignCancellationListRequest(
    @SerializedName("campaign_type")
    val sellerCampaignType: Int
)
