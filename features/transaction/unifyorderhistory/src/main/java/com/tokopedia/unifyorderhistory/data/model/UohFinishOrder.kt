package com.tokopedia.unifyorderhistory.data.model

import com.google.gson.annotations.SerializedName

data class UohFinishOrder(
    @SerializedName("finish_order_buyer")
    val finishOrderBuyer: FinishOrderBuyer = FinishOrderBuyer()
) {
    data class FinishOrderBuyer(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("message")
        val message: List<String> = listOf()
    )
}
