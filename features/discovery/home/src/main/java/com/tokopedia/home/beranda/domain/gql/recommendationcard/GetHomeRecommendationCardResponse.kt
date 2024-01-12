package com.tokopedia.home.beranda.domain.gql.recommendationcard

import com.google.gson.annotations.SerializedName

data class GetHomeRecommendationCardResponse(
    @SerializedName("getHomeRecommendationCard")
    val getHomeRecommendationCard: GetHomeRecommendationCard = GetHomeRecommendationCard()
) {
    data class GetHomeRecommendationCard(
        @SerializedName("cards")
        val recommendationCards: List<RecommendationCard> = listOf(),
        @SerializedName("hasNextPage")
        val hasNextPage: Boolean = false,
        @SerializedName("layoutName")
        val layoutName: String = "",
        @SerializedName("pageName")
        val pageName: String = ""
    )
}
