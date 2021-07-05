package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListWaitingPaymentResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data = Data()
) {
    data class Data(
        @SerializedName("orderFilterSom")
        @Expose
        val orderFilterSom: OrderFilterSom = OrderFilterSom()
    ) {
        data class OrderFilterSom(
            @SerializedName("waiting_payment_counter")
            @Expose
            val waitingPaymentCounter: WaitingPaymentCounter = WaitingPaymentCounter()
        ) {
            data class WaitingPaymentCounter(
                @SerializedName("amount")
                @Expose
                val amount: Int = 0,
                @SerializedName("text")
                @Expose
                val text: String = ""
            )
        }
    }
}