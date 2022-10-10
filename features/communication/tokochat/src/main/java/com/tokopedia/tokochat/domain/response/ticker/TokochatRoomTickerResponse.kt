package com.tokopedia.tokochat.domain.response.ticker


import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class TokochatRoomTickerResponse(
    @SerializedName("tokochatRoomTicker")
    val tokochatRoomTicker: TokochatRoomTicker = TokochatRoomTicker()
) {
    data class TokochatRoomTicker(
        @SerializedName("enable")
        val enable: Boolean = false,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("tickerType")
        val tickerType: Int = Int.ZERO
    )
}
