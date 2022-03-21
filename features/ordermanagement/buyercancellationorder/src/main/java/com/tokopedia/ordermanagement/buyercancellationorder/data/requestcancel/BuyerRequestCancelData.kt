package com.tokopedia.ordermanagement.buyercancellationorder.data.requestcancel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuyerRequestCancelData(
    @Expose
    @SerializedName("data")
    val data: Data = Data()
) {

    data class Data(
        @Expose
        @SerializedName("buyer_request_cancel")
        val buyerRequestCancel: BuyerRequestCancel = BuyerRequestCancel()
    ) {

        data class BuyerRequestCancel(
            @Expose
            @SerializedName("success")
            val success: Int = -1,
            @Expose
            @SerializedName("message")
            val message: List<String> = listOf(),
            @Expose
            @SerializedName("popup")
            val popup: Popup = Popup()
        ) {

            data class Popup(
                @Expose
                @SerializedName("title")
                val title: String = "",
                @Expose
                @SerializedName("body")
                val body: String = ""
            )
        }
    }
}