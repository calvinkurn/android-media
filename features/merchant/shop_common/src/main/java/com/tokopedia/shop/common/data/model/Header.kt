package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("ctaText")
    val ctaText: String = "",
    @SerializedName("ctaLink")
    val ctaLink: String = "",
    @SerializedName("isATC")
    val isAtc: Int = -1,
    @SerializedName("etalaseID")
    val etalaseId: String = "",
    @SerializedName("isShowEtalaseName")
    val isShowEtalaseName: Int = -1,
    @SerializedName("data")
    val data: List<HeaderData> = listOf()
)
