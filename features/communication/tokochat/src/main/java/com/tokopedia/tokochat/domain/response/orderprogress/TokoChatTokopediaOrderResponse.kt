package com.tokopedia.tokochat.domain.response.orderprogress

import com.google.gson.annotations.SerializedName

data class TokoChatTokopediaOrderResponse(
    @SerializedName("tokochatTokopediaOrder")
    val tokopediaOrder: TokoChatTokopediaOrder = TokoChatTokopediaOrder()
)

data class TokoChatTokopediaOrder(
    @SerializedName("tokopediaOrderID")
    val tokopediaOrderId: String = ""
)
