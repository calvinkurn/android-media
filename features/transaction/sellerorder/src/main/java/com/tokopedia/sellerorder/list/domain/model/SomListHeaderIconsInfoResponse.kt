package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListHeaderIconsInfoResponse(
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
            val waitingPaymentCounter: WaitingPaymentCounter = WaitingPaymentCounter(),
            @SerializedName("seller_info")
            @Expose
            val sellerInfo: SellerInfo? = null
        ) {
            data class WaitingPaymentCounter(
                @SerializedName("amount")
                @Expose
                val amount: Int = 0,
                @SerializedName("text")
                @Expose
                val text: String = ""
            )

            data class SellerInfo(
                @SerializedName("plus_logo")
                @Expose
                val plusLogo: String? = null,
                @SerializedName("edu_url")
                @Expose
                val eduUrl: String? = null
            )
        }
    }
}