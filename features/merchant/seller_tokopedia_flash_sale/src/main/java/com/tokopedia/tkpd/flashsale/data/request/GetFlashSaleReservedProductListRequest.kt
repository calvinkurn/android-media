package com.tokopedia.tkpd.flashsale.data.request


import com.google.gson.annotations.SerializedName

data class GetFlashSaleReservedProductListRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
    @SerializedName("reservation_id")
    val reservationId: String,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("filter")
    val filter: Filter,
) {
    data class Pagination(
        @SerializedName("rows")
        val rows: Int,
        @SerializedName("offset")
        val offset: Int
    )
    data class Filter(
        @SerializedName("keyword")
        val keyword: String,
        @SerializedName("category_ids")
        val categoryIds: List<Long>,
    )
}
