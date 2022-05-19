package com.tokopedia.shop.flash_sale.data.request

import com.google.gson.annotations.SerializedName

data class GetSellerProductListMetaRequest(
    @SerializedName("seller_campaign_type")
    val sellerCampaignType: Int
)