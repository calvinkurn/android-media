package com.tokopedia.buyerorder.detail.data.requestcancel


import com.google.gson.annotations.SerializedName

data class BuyerRequestCancelData(
    @SerializedName("data")
    val data: Data = Data()) {

        data class Data(
                @SerializedName("buyer_request_cancel")
                val buyerRequestCancel: BuyerRequestCancel = BuyerRequestCancel()) {

                data class BuyerRequestCancel(
                        @SerializedName("success")
                        val success: Int = -1,
                        @SerializedName("message")
                        val message: List<String> = listOf(),
                        @SerializedName("popup")
                        val popup: Popup = Popup()) {

                        data class Popup(
                                @SerializedName("title")
                                val title: String = "",
                                @SerializedName("body")
                                val body: String = "")
                }
        }
}