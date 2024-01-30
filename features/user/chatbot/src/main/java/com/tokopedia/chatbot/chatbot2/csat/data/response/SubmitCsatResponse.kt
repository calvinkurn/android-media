package com.tokopedia.chatbot.chatbot2.csat.data.response

import com.google.gson.annotations.SerializedName

data class SubmitCsatResponse(
    @SerializedName("data")
    val data: SubmitCsatData = SubmitCsatData()
)

data class SubmitCsatData(
    @SerializedName("isSuccess")
    val isSuccess: Int = 0,
    @SerializedName("toasterMessage")
    val toasterMessage: String = ""
)
