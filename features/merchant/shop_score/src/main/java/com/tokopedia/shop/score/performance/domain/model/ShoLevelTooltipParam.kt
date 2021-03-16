package com.tokopedia.shop.score.performance.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShoLevelTooltipParam (
        @Expose
        @SerializedName("shopID")
        val shopID: String = "",
        @Expose
        @SerializedName("source")
        val source: String = "gql"
)