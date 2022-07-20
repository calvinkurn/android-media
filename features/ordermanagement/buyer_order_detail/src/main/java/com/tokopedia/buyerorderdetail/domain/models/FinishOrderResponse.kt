package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FinishOrderResponse(
    @Expose
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @Expose
        @SerializedName("finish_order_buyer")
        val finishOrderBuyer: FinishOrderBuyer = FinishOrderBuyer()
    ) {
        data class FinishOrderBuyer(
            @Expose
            @SerializedName("success")
            val success: Int = 0,
            @Expose
            @SerializedName("message")
            val message: List<String> = listOf()
        )
    }
}