package com.tokopedia.recharge_slice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeFavoriteRecommendationList (
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("recommendations")
    @Expose
    val recommendations: List<Recommendation> = listOf()
)