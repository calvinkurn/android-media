package com.tokopedia.chatbot.domain.pojo.submitchatcsat

import com.google.gson.annotations.SerializedName


data class ChipSubmitChatCsatInput(
        @SerializedName("caseChatID")
        var caseChatID: String = "",
        @SerializedName("caseID")
        var caseID: String = "",
        @SerializedName("messageID")
        var messageID: String = "",
        @SerializedName("rating")
        var rating: Int = 0,
        @SerializedName("reasonCode")
        var reasonCode: String = ""
)