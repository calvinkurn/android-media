package com.tokopedia.chatbot.chatbot2.data.TickerData

import com.google.gson.annotations.SerializedName

data class ChipGetActiveTickerV4(

    @SerializedName("server_process_time")
    val serverProcessTime: String? = "",

    @SerializedName("data")
    val data: TickerData? = null,

    @SerializedName("message_error")
    val messageError: List<Any?>? = null,

    @SerializedName("status")
    val status: String? = ""
)
