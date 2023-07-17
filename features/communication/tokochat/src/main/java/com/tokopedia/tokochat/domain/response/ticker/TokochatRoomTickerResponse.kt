package com.tokopedia.tokochat.domain.response.ticker

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class TokochatRoomTickerResponse(
    @SerializedName("tokochatRoomTicker")
    val tokochatRoomTicker: TokochatRoomTicker = TokochatRoomTicker()
) {
    data class TokochatRoomTicker(
        @SerializedName("enable")
        var enable: Boolean = false,
        @SerializedName("message")
        var message: String = "",
        @SerializedName("tickerType")
        var tickerType: Int = Int.ZERO
    )
}
