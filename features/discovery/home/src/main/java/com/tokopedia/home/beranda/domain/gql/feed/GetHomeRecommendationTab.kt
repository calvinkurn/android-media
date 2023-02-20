package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetHomeRecommendationTab(
    @SerializedName("recommendation_tabs", alternate = ["tabs"])
    @Expose
    val recommendationTabs: List<RecommendationTab> = listOf()
)
