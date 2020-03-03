package com.tokopedia.reviewseller.feature.reviewlist.data

import com.google.gson.annotations.SerializedName

data class RatingData(
        @SerializedName("productrevShopRatingAggregate")
        val productShopRatingAggregate: ProductShopRatingAggregate
)