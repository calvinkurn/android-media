package com.tokopedia.chatbot.chatbot2.csat.data.request

import com.google.gson.annotations.SerializedName

data class SubmitCsatRequest(
    @SerializedName("messageID")
    val messageID: String = "",
    @SerializedName("caseID")
    val caseID: String = "",
    @SerializedName("caseChatID")
    val caseChatID: String = "",
    @SerializedName("rating")
    val rating: Int = 0,
    @SerializedName("reasonCode")
    val reasonCode: String = "",
    @SerializedName("category")
    val category: String = "",
    @SerializedName("otherReason")
    val otherReason: String = "",
    @SerializedName("dynamicReasons")
    val dynamicReasons: List<String> = emptyList()
)
