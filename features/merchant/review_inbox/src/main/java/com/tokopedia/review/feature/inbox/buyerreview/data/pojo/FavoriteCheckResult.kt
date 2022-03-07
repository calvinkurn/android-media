package com.tokopedia.review.feature.inbox.buyerreview.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavoriteCheckResult(
    @SerializedName("data")
    @Expose
    val shopIds: List<String> = listOf()
)