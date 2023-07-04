package com.tokopedia.shop.score.penalty.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ONE

data class ShopScorePenaltyDetailParam(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("total")
    val total: Int? = 10,
    @SerializedName("startDate")
    val startDate: String = "",
    @SerializedName("endDate")
    val endDate: String = "",
    @SerializedName("typeID")
    val typeID: Int? = null,
    @SerializedName("typeIDs")
    val typeIDs: List<Int> = listOf(),
    @SerializedName("sort")
    val sort: Int? = null,
    @SerializedName("lang")
    val lang: String = "id",
    @SerializedName("source")
    val source: String = "android-shop-penalty",
    @SerializedName("status")
    val status: Int = Int.ONE
)
