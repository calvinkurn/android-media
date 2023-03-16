package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeFeedTabGqlResponse(
    @SerializedName("get_home_recommendation", alternate = ["getHomeRecommendationTabV2"])
    @Expose
    val homeRecommendation: GetHomeRecommendationTab = GetHomeRecommendationTab()
)
