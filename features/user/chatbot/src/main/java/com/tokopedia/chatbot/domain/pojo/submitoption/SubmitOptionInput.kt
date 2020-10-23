package com.tokopedia.chatbot.domain.pojo.submitoption


import com.google.gson.annotations.SerializedName

data class SubmitOptionInput(
    var caseChatID: String = "",
    var caseID: String = "",
    var messageID: String = "",
    var source: String = "",
    var value: Int = 0
)