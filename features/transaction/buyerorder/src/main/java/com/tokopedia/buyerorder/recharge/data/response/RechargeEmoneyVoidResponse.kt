package com.tokopedia.buyerorder.recharge.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

class RechargeEmoneyVoidResponse(
    @field:SerializedName("status")
    val status: Int = 0,
    @field:SerializedName("message")
    val message: String = ""
) {
    var isNeedRefresh: Boolean = false

    class Response(
        @SuppressLint("Invalid Data Type")
        @field:SerializedName("rechargeEmoneyVoid")
        val rechargeEmoneyVoid: RechargeEmoneyVoidResponse = RechargeEmoneyVoidResponse()
    )
}