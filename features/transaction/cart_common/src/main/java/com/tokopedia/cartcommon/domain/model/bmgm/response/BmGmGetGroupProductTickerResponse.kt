package com.tokopedia.cartcommon.domain.model.bmgm.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.cartcommon.data.response.bmgm.BmGmData

data class BmGmGetGroupProductTickerResponse(

    @SerializedName("get_group_product_ticker")
    val getGroupProductTicker: GetGroupProductTicker = GetGroupProductTicker()
) {
    data class GetGroupProductTicker(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),

        @SerializedName("data")
        val data: Data = Data(),

        @SerializedName("status")
        val status: String = ""
    ) {
        data class Data(
            @SerializedName("multiple_data")
            val multipleData: List<MultipleData> = emptyList()
        ) {
            data class Icon(
                @SerializedName("url")
                val url: String = ""
            )

            data class Message(
                @SerializedName("text")
                val text: String = "",

                @SerializedName("url")
                val url: String = ""
            )

            data class MultipleData(
                @SerializedName("type")
                val type: String = "",

                @SerializedName("action")
                val action: String = "",

                @SerializedName("cart_string_order")
                val cartStringOrder: String = "",

                @SerializedName("offer_id")
                val offerId: String = "",

                @SerializedName("icon")
                val icon: Icon = Icon(),

                @SerializedName("message")
                val listMessage: List<Message> = emptyList(),

                @SerializedName("discount_amount")
                val discountAmount: Double = 0.0,

                @SerializedName("bmgm")
                val bmgmData: BmGmData = BmGmData()
            )
        }
    }
}
