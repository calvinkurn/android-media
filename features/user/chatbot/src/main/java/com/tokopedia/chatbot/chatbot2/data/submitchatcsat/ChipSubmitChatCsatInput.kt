package com.tokopedia.chatbot.chatbot2.data.submitchatcsat

import com.google.gson.annotations.SerializedName

data class ChipSubmitChatCsatInput(
    @SerializedName("messageID")
    var messageID: String = "",
    @SerializedName("caseID")
    var caseID: String = "",
    @SerializedName("caseChatID")
    var caseChatID: String = "",
    @SerializedName("rating")
    var rating: Long = 0,
    @SerializedName("reasonCode")
    var reasonCode: String = "",
    @SerializedName("service")
    var service: String = "",
    @SerializedName("otherReason")
    var otherReason: String = "",
    @SerializedName("dynamicReasons")
    var dynamicReasons: List<String> = emptyList()
)
