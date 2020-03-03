package com.tokopedia.reviewseller.feature.reviewlist.data

import com.google.gson.annotations.SerializedName

data class ProductShopRatingAggregate(
        @SerializedName("hasNext") val hasNext : Boolean,
        @SerializedName("data") val data : List<RatingData>
)