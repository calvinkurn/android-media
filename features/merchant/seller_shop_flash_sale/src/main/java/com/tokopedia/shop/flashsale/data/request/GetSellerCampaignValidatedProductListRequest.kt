package com.tokopedia.shop.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetSellerCampaignValidatedProductListRequest(
    @SerializedName("campaign_type")
    val campaignType: Int,
    @SuppressLint("Invalid Data Type") // BE still using number
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("filter")
    val filter: Filter,
) {
    class Filter (
        @SerializedName("page")
        val page: Int,
        @SerializedName("page_size")
        val pageSize: Int,
        @SerializedName("keyword")
        val keyword: String,
    )
}