package com.tokopedia.shop.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetSellerCampaignListRequest(
    @SuppressLint("Invalid Data Type") @SerializedName("shop_id")
    val shopId: Long,
    @SerializedName("seller_campaign_type")
    val sellerCampaignType: Int,
    @SerializedName("list_type")
    val listType: Int,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("filter")
    val filter: Filter
) {
    data class Pagination(
        @SerializedName("rows") val rows: Int,
        @SerializedName("offset") val offset: Int
    )

    data class Filter(
        @SuppressLint("Invalid Data Type") @SerializedName("status_id") val status_id: List<Int>,
        @SuppressLint("Invalid Data Type") @SerializedName("campaign_id") val campaign_id: Int,
        @SerializedName("campaign_name") val campaignName: String,
        @SerializedName("thematic_participation") val thematicParticipation: Boolean
    )
}