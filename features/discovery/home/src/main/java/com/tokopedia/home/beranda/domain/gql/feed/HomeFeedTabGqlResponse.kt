package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeFeedTabGqlResponse (
    @SerializedName("get_home_recommendation")
    @Expose
    val homeRecommendation: GetHomeRecommendationTab = GetHomeRecommendationTab()
)