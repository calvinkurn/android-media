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
    val filter: Filter,
    @SerializedName("sort")
    val sort: Sort
) {
    data class Pagination(
        @SerializedName("rows") val rows: Int,
        @SerializedName("offset") val offset: Int
    )

    data class Sort(
        @SerializedName("order_by") val orderBy: Int,
        @SerializedName("order_rule") val orderRule: Int
    )

    data class Filter(
        @SuppressLint("Invalid Data Type") @SerializedName("status_id") val status_id: List<Int>,
        @SuppressLint("Invalid Data Type") @SerializedName("campaign_id") val campaignId: Long,
        @SerializedName("campaign_name") val campaignName: String
    )
}