package com.tokopedia.cartrevamp.domain.model.bmgm.response

import com.google.gson.annotations.SerializedName

data class BmGmGetGroupProductTickerResponse(
    @SerializedName("error_message")
    val errorMessage: List<String> = emptyList(),

    @SerializedName("data")
    val data: Data = Data(),

    @SerializedName("status")
    val status: String = ""
) {
    data class Data(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("action")
        val action: String = "",

        @SerializedName("icon")
        val icon: Icon = Icon(),

        @SerializedName("message")
        val listMessage: List<Message> = emptyList(),

        @SerializedName("discount_amount")
        val discountAmount: Double = 0.0
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
    }
}
