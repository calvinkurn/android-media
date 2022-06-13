package com.tokopedia.ordermanagement.buyercancellationorder.data.instantcancellation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuyerInstantCancelData(
    @Expose
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @Expose
        @SerializedName("buyer_instant_cancel")
        val buyerInstantCancel: BuyerInstantCancel = BuyerInstantCancel()
    ) {
        data class BuyerInstantCancel(
            @Expose
            @SerializedName("success")
            val success: Int = 0,
            @Expose
            @SerializedName("message")
            val message: String = "",
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