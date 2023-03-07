package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuyerHistoryData(
        @SerializedName("history_img")
        var historyImg: String = "",

        @SerializedName("history_title")
        var historyTitle: String = "",

        @SerializedName("histories")
        var histories: List<History> = emptyList()
) {
    fun isNotEmpty(): Boolean = historyImg.isNotEmpty() and historyTitle.isNotEmpty() and histories.isNotEmpty()
}