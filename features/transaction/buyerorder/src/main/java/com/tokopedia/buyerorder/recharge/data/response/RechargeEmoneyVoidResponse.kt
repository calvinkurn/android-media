package com.tokopedia.buyerorder.recharge.data.response

import com.google.gson.annotations.SerializedName

class RechargeEmoneyVoidResponse(
    @field:SerializedName("status")
    val status: Int = 0,
    @field:SerializedName("message")
    val message: String = ""
) {
    class Response(
        @field:SerializedName("rechargeEmoneyVoid")
        val rechargeEmoneyVoid: RechargeEmoneyVoidResponse = RechargeEmoneyVoidResponse()
    )
}