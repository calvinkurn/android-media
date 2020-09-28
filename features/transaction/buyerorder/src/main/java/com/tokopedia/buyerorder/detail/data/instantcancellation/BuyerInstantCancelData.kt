package com.tokopedia.buyerorder.detail.data.instantcancellation
import com.google.gson.annotations.SerializedName

data class BuyerInstantCancelData(
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("buyer_instant_cancel")
        val buyerInstantCancel: BuyerInstantCancel = BuyerInstantCancel()
    ) {
        data class BuyerInstantCancel(
            @SerializedName("success")
            val success: Int = 0,
            @SerializedName("message")
            val message: String = "",
            @SerializedName("popup")
            val popup: Popup = Popup()
        ) {
            data class Popup(
                @SerializedName("title")
                val title: String = "",
                @SerializedName("body")
                val body: String = ""
            )
        }
    }
}