package com.tokopedia.buyerorder.detail.data.finishorder


import com.google.gson.annotations.SerializedName

data class BuyerFinishOrder(
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
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
}