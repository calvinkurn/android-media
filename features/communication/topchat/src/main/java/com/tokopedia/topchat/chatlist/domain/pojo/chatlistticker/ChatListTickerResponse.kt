package com.tokopedia.topchat.chatlist.domain.pojo.chatlistticker


import com.google.gson.annotations.SerializedName

data class ChatListTickerResponse(
    @SerializedName("chatlistTicker")
    val chatlistTicker: ChatListTicker = ChatListTicker()
) {
    data class ChatListTicker(
        @SerializedName("tickerBuyer")
        val tickerBuyer: TickerBuyer = TickerBuyer(),
        @SerializedName("tickerSeller")
        val tickerSeller: TickerSeller = TickerSeller()
    ) {
        data class TickerBuyer(
            @SerializedName("enable")
            val enable: Boolean = false,
            @SerializedName("message")
            val message: String = "",
            @SerializedName("tickerType")
            val tickerType: Int = 0
        )

        data class TickerSeller(
            @SerializedName("enable")
            val enable: Boolean = false,
            @SerializedName("message")
            val message: String = "",
            @SerializedName("tickerType")
            val tickerType: Int = 0
        )
    }
}
