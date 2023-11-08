package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class TopadsGetBudgetRecommendationResponse(
    @SerializedName("topadsGetBudgetRecommendation")
    val topadsGetBudgetRecommendation: TopadsGetBudgetRecommendation
){}

data class TopadsGetBudgetRecommendation(
    @SerializedName("data")
    val data: Data,

    @SerializedName("errors")
    val errors: List<Error> = listOf()
) {
    data class Data(

        @SerializedName("minBudgetRecommendation")
        val minBudgetRecommendation: Int,

        @SerializedName("maxBudgetRecommendation")
        val maxBudgetRecommendation: Int,

        @SerializedName("isEligible")
        val isEligible: Boolean

    )
}
