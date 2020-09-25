package com.tokopedia.navigation_common.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 18/07/20.
 */
data class UohCounterModel (
        @SerializedName("uohOrderCount")
        val uohOrderCount: UohOrderCount = UohOrderCount()
) {
    data class UohOrderCount(
            @SerializedName("onProcess")
            val onProcess: String = "",
            @SerializedName("onProcessText")
            val onProcessText: String = "",
            @SerializedName("activeTickets")
            val activeTickets: String = "",
            @SerializedName("activeTicketsText")
            val activeTicketsText: String = ""
    )
}