package com.tokopedia.shop.score.penalty.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScorePenaltyDetailParam(
        @Expose
        @SerializedName("page")
        val page: Int = 1,
        @Expose
        @SerializedName("total")
        val total: Int = 10,
        @SerializedName("startDate")
        @Expose
        val startDate: String = "",
        @SerializedName("endDate")
        @Expose
        val endDate: String = "",
        @SerializedName("typeID")
        @Expose
        val typeID: Int = 0,
        @SerializedName("sort")
        @Expose
        val sort: Int = 0,
        @SerializedName("lang")
        @Expose
        val lang: String = "id",
        @SerializedName("source")
        @Expose
        val source: String = "android-shop-penalty"
)