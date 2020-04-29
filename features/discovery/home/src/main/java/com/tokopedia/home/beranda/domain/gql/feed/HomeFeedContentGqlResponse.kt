package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeFeedContentGqlResponse (
    @SerializedName("get_home_recommendation")
    @Expose
    val homeRecommendation: GetHomeRecommendationContent = GetHomeRecommendationContent()
)