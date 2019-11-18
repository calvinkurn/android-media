package com.tokopedia.chatbot.data.TickerData

import com.google.gson.annotations.SerializedName


data class ChipGetActiveTickerV4(

        @SerializedName("server_process_time")
        val serverProcessTime: String? = null,

        @SerializedName("data")
        val data: TickerData? = null,

        @SerializedName("message_error")
        val messageError: List<Any?>? = null,

        @SerializedName("status")
        val status: String? = null
)