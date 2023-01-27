package com.tokopedia.chatbot.chatbot2.data.csatRating.csatResponse
import com.google.gson.annotations.SerializedName

data class Header(

    @SerializedName("reason")
    val reason: String? = null,

    @SerializedName("messages")
    val messages: List<Any?>? = null,

    @SerializedName("error_code")
    val errorCode: String? = null,

    @SerializedName("is_success")
    val isSuccess: Boolean? = null
)
