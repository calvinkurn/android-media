package com.tokopedia.home.beranda.domain.gql.recommendationcard

import com.google.gson.annotations.SerializedName

data class GetHomeRecommendationCardResponse(
    @SerializedName("getHomeRecommendationCard")
    val getHomeRecommendationCard: GetHomeRecommendationCard = GetHomeRecommendationCard()
) {
    data class GetHomeRecommendationCard(
        @SerializedName("appLog")
        val appLog: AppLog = AppLog(),
        @SerializedName("cards")
        val recommendationCards: List<RecommendationCard> = listOf(),
        @SerializedName("hasNextPage")
        val hasNextPage: Boolean = false,
        @SerializedName("layoutName")
        val layoutName: String = "",
        @SerializedName("pageName")
        val pageName: String = ""
    ) {
        data class AppLog(
            @SerializedName("bytedanceSessionID")
            val sessionId: String = "",
            @SerializedName("requestID")
            val requestId: String = "",
            @SerializedName("logID")
            val logId: String = ""
        )
    }
}
