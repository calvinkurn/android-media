package com.tokopedia.home_recom.model.entity


import com.google.gson.annotations.SerializedName

data class RecommendationProductDetail(
        @SerializedName("data")
        val `data`: List<Data>
)