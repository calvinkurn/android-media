package com.tokopedia.chatbot.data.TickerData

import com.google.gson.annotations.SerializedName

data class ItemsItem(

        @SerializedName("text")
        val text: String? = "",

        @SerializedName("title")
        val title: String? = ""
)