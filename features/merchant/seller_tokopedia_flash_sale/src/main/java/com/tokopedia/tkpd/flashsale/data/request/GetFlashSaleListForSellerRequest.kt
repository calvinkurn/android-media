package com.tokopedia.tkpd.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetFlashSaleListForSellerRequest(
    @SerializedName("request_header")
    val requestHeader: CampaignParticipationRequestHeader,
    @SerializedName("tab_name")
    val tabName: String,
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("filter")
    val filter: Filter,
    @SerializedName("sort")
    val sort: Sort,
    @SerializedName("additional_params")
    val additionalParams: AdditionalParam
) {
    data class Pagination(
        @SerializedName("rows")
        val rows: Int,
        @SerializedName("offset")
        val offset: Int
    )

    data class Filter(
        @SerializedName("campaign_ids")
        val campaignIds: List<Long>,
        @SerializedName("category_ids")
        val categoryIds: List<Long>,
        @SerializedName("status_ids")
        val statusIds: List<String>
    )

    data class Sort(
        @SerializedName("order_by")
        val orderBy: String,
        @SerializedName("order_rule")
        val orderRule: String,
    )

    data class AdditionalParam(
        @SerializedName("highlight_recommendation")
        val highlightRecommendation: Boolean = false,
        @SerializedName("product_meta")
        val productMeta: Boolean = false,
        @SerializedName("check_product_eligibility")
        val checkProductEligibility: Boolean = true,
        @SerializedName("matched_product_per_criteria")
        val matchedProductPerCriteria: Boolean = true
    )
}
