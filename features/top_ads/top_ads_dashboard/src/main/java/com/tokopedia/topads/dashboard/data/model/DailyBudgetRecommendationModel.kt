package com.tokopedia.topads.dashboard.data.model


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class DailyBudgetRecommendationModel(
        @SerializedName("topadsGetDailyBudgetRecommendation")
        val topadsGetDailyBudgetRecommendation: TopadsGetDailyBudgetRecommendation = TopadsGetDailyBudgetRecommendation()
)

data class TopadsGetDailyBudgetRecommendation(
        @SerializedName("data")
        val data: List<DataBudget> = listOf(),
        @SerializedName("errors")
        val errors: List<Error> = listOf()
)

data class DataBudget(
        @SerializedName("avg_bid")
        val avgBid: String = "0",
        @SerializedName("group_id")
        val groupId: String = "0",
        @SerializedName("group_name")
        val groupName: String = "",
        @SerializedName("price_daily")
        val priceDaily: Double = 0.0,
        @SerializedName("suggested_price_daily")
        val suggestedPriceDaily: Double = 0.0,
        var setCurrentBid: Double = 0.0
)