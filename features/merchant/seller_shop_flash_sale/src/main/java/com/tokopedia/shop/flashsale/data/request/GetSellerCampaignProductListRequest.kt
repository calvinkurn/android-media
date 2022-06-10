package com.tokopedia.shop.flashsale.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetSellerCampaignProductListRequest(
    @SerializedName("campaign_type")
    val campaignType: Long = 0,
    @SerializedName("list_type")
    val listType: Int = 0,
    @SerializedName("pagination")
    val pagination: Pagination = Pagination(),
    @SerializedName("filter")
    val filter: Filter = Filter(),
    @SerializedName("sort")
    val sort: Sort = Sort()
) {
    data class Pagination(
        @SerializedName("rows")
        val rows: Int = 50,
        @SerializedName("offset")
        val offset: Int = 0
    )

    data class Filter(
        @SuppressLint("Invalid Data Type")
        @SerializedName("campaign_id")
        val campaignId: Long = 0,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("category_ids")
        val categoryIds: Int = 0
    )

    data class Sort(
        @SerializedName("order_by")
        val orderBy: Int = 0,
        @SerializedName("order_rule")
        val orderRule: Int = 0
    )
}
