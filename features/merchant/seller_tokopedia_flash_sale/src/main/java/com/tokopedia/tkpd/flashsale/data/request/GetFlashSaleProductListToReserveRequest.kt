package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductListToReserveRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("list_type")
    val listType: String,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("filter")
    val filter: Filter
) {
    data class Pagination(
        @SerializedName("rows")
        val rows: Int,
        @SerializedName("offset")
        val offset: Int
    )

    data class Filter(
        @SerializedName("category_ids")
        val categoryIds: List<Long>,
        @SerializedName("city_ids")
        val cityIds: List<Long>,
        @SerializedName("keyword")
        val keyword: String
    )
}