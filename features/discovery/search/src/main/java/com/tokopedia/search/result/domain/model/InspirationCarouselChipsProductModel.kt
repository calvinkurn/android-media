package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselProduct

data class InspirationCarouselChipsProductModel(
        @SerializedName("searchProductCarouselByIdentifier")
        @Expose
        val searchProductCarouselByIdentifier: SearchProductCarouselByIdentifier = SearchProductCarouselByIdentifier(),
) {

    data class SearchProductCarouselByIdentifier(
            @SerializedName("product")
            @Expose
            val product: List<InspirationCarouselProduct> = listOf(),
    )
}