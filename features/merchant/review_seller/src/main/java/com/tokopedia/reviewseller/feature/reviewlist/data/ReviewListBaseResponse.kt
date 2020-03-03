package com.tokopedia.reviewseller.feature.reviewlist.data

import com.google.gson.annotations.SerializedName

data class ReviewListBaseResponse(
        @SerializedName("data") val ratingData: RatingData
)