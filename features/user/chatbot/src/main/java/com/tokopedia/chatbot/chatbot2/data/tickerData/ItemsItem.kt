package com.tokopedia.chatbot.chatbot2.data.tickerData

import com.google.gson.annotations.SerializedName

data class ItemsItem(

    @SerializedName("text")
    val text: String? = "",

    @SerializedName("title")
    val title: String? = ""
)
