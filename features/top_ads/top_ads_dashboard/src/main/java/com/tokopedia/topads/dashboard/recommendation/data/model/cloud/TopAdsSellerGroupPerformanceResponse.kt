package com.tokopedia.topads.dashboard.recommendation.data.model.cloud


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error
import com.tokopedia.topads.dashboard.recommendation.data.model.local.GroupPerformanceWidgetUiModel

data class TopAdsSellerGroupPerformanceResponse(
    @SerializedName("GetSellerGroupPerformance")
    val getSellerGroupPerformance: GetSellerGroupPerformance = GetSellerGroupPerformance()
) {
    data class GetSellerGroupPerformance(
        @SerializedName("data")
        val sellerGroupPerformance: SellerGroupPerformance = SellerGroupPerformance(),
        @SerializedName("errors")
        val errors: List<Error> = listOf()
    ) {
        data class SellerGroupPerformance(
            @SerializedName("click_growth")
            val clickGrowth: Int = 0,
            @SerializedName("generalised_insight_category")
            val generalisedInsightCategory: String = "",
            @SerializedName("group_id")
            val groupId: String = "",
            @SerializedName("impression")
            val impression: Int = 0,
            @SerializedName("impression_growth")
            val impressionGrowth: Int = 0,
            @SerializedName("percent_impression_growth")
            val percentImpressionGrowth: Int = 0,
            @SerializedName("percent_roas_growth")
            val percentRoasGrowth: Int = 0,
            @SerializedName("percent_total_sold_growth")
            val percentTotalSoldGrowth: Int = 0,
            @SerializedName("top_slot_impression")
            val topSlotImpression: Int = 0,
            @SerializedName("total_sold_growth")
            val totalSoldGrowth: Int = 0
        )
    }

    fun toGroupPerformanceWidgetUiModel(): GroupPerformanceWidgetUiModel {
        val performance = this.getSellerGroupPerformance.sellerGroupPerformance
        return GroupPerformanceWidgetUiModel(
            impression = performance.impression,
            topSlotImpression = performance.topSlotImpression,
        )
    }
}
