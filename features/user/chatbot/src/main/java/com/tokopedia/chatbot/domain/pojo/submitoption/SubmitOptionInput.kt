package com.tokopedia.chatbot.domain.pojo.submitoption


import com.google.gson.annotations.SerializedName

data class SubmitOptionInput(
        @SerializedName("caseChatID")
        var caseChatID: String = "",
        @SerializedName("caseID")
        var caseID: String = "",
        @SerializedName("messageID")
        var messageID: String = "",
        @SerializedName("source")
        var source: String = "",
        @SerializedName("value")
        var value: Int = 0
)