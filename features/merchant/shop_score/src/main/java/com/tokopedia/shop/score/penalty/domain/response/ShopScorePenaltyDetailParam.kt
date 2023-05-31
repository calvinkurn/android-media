package com.tokopedia.shop.score.penalty.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ONE

data class ShopScorePenaltyDetailParam(
        @Expose
        @SerializedName("page")
        val page: Int? = null,
        @Expose
        @SerializedName("total")
        val total: Int? = 10,
        @SerializedName("startDate")
        @Expose
        val startDate: String = "",
        @SerializedName("endDate")
        @Expose
        val endDate: String = "",
        @SerializedName("typeID")
        @Expose
        val typeID: Int? = null,
        @SerializedName("sort")
        @Expose
        val sort: Int? = null,
        @SerializedName("lang")
        @Expose
        val lang: String = "id",
        @SerializedName("source")
        @Expose
        val source: String = "android-shop-penalty",
        @SerializedName("status")
        val status: Int = Int.ONE
)
