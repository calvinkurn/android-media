package com.tokopedia.topads.detail_sheet.data.model


import com.google.gson.annotations.SerializedName

data class Page(
    @SerializedName("current")
    val current: Int = 0,
    @SerializedName("max")
    val max: Int = 0,
    @SerializedName("min")
    val min: Int = 0,
    @SerializedName("per_page")
    val perPage: Int = 0,
    @SerializedName("total")
    val total: Int = 0
)