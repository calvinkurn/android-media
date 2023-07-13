package com.tokopedia.topads.dashboard.recommendation.data.model.cloud


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class TopAdsGetSellerInsightDataResponse(
    @SerializedName("GetSellerInsightData")
    val getSellerInsightData: GetSellerInsightData = GetSellerInsightData()
) {
    data class GetSellerInsightData(
        @SerializedName("data")
        val sellerInsightData: SellerInsightData = SellerInsightData(),
        @SerializedName("errors")
        val errors: List<Error> = listOf()
    ) {
        data class SellerInsightData(
            @SerializedName("daily_budget_data")
            val dailyBudgetData: List<DailyBudgetData> = listOf(),
            @SerializedName("total_potensial_click")
            val totalPotensialClick: Int = 0
        ) {
            data class DailyBudgetData(
                @SerializedName("avg_bid")
                val avgBid: Int = 0,
                @SerializedName("group_id")
                val groupId: String = "",
                @SerializedName("group_name")
                val groupName: String = "",
                @SerializedName("impression")
                val impression: Int = 0,
                @SerializedName("potential_click")
                val potentialClick: Int = 0,
                @SerializedName("price_daily")
                val priceDaily: Int = 0,
                @SerializedName("suggested_price_daily")
                val suggestedPriceDaily: Int = 0,
                @SerializedName("top_slot_impression")
                val topSlotImpression: Int = 0
            )
        }
    }
}
