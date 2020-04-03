package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetHomeRecommendationContent (
    @SerializedName("recommendation_product")
    @Expose
    val recommendationProduct: RecommendationProduct = RecommendationProduct()
)