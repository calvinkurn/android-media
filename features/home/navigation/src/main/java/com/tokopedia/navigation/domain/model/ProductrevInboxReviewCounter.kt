package com.tokopedia.navigation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevInboxReviewCounter(
        @SerializedName("count")
        @Expose
        val count: Int = 0
)