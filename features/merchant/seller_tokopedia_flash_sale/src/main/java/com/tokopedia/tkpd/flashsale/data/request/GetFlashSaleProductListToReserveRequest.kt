package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetFlashSaleProductListToReserveRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
    @SerializedName("campaign_id")
    val campaignId: Long = 0L,
    @SerializedName("list_type")
    val listType: String = "",
    @SerializedName("pagination")
    val pagination: Pagination = Pagination(),
    @SerializedName("filter")
    val filter: Filter = Filter()
) {
    data class Pagination(
        @SerializedName("rows")
        val rows: Int = 0,
        @SerializedName("offset")
        val offset: Int = 0
    )

    data class Filter(
        @SerializedName("category_ids")
        val categoryIds: List<Long> = emptyList(),
        @SerializedName("city_ids")
        val cityIds: List<Long> = emptyList(),
        @SerializedName("keyword")
        val keyword: String = ""
    )
}