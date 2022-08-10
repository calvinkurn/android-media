package com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("history_img")
        @Expose
        var historyImg: String = "",

        @SerializedName("history_title")
        @Expose
        var historyTitle: String = "",

        @SerializedName("histories")
        @Expose
        var histories: List<History> = emptyList()
) {
    fun isNotEmpty(): Boolean = historyImg.isNotEmpty() and historyTitle.isNotEmpty() and histories.isNotEmpty()
}