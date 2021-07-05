package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationCarousel

data class SearchInspirationCarouselModel(
        @SerializedName("searchInspirationCarouselV2")
        @Expose
        val searchInspirationCarousel: SearchInspirationCarousel = SearchInspirationCarousel()
)