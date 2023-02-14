package com.tokopedia.purchase_platform.common.feature.tickerannouncement

import com.google.gson.annotations.SerializedName

data class Ticker(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("page")
    val page: String = "",
    @SerializedName("title")
    val title: String = ""
) {
    fun isValid(page: String?): Boolean {
        return (page == null || page.equals(this.page, true)) && message.isNotBlank()
    }
}
