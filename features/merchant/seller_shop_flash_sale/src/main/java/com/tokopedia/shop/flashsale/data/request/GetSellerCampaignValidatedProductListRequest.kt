package com.tokopedia.shop.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

@SuppressLint("Invalid Data Type")
data class GetSellerCampaignValidatedProductListRequest(
    @SerializedName("campaign_type")
    val campaignType: Int,
    @SerializedName("campaign_id")
    val campaignId: Int,
    @SerializedName("filter")
    val filter: Filter,
) {
    class Filter (
        @SerializedName("page")
        val page: Int,
        @SerializedName("page_size")
        val pageSize: Int,
    )
}