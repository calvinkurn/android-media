package com.tokopedia.purchase_platform.common.feature.tickerannouncement


import com.google.gson.annotations.SerializedName

data class Ticker(
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("page")
    val page: String,
    @field:SerializedName("title")
    val title: String
)