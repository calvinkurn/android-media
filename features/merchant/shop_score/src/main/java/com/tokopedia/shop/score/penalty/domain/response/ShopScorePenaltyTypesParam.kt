package com.tokopedia.shop.score.penalty.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScorePenaltyTypesParam(
        @Expose
        @SerializedName("lang")
        val lang: String = "id",
        @Expose
        @SerializedName("source")
        val source: String = "sellerapp"
)