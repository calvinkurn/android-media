package com.tokopedia.chatbot.domain.pojo.submitchatcsat


data class ChipSubmitChatCsatInput(
        var caseChatID: String = "",
        var caseID: String = "",
        var messageID: String = "",
        var rating: Int = 0,
        var reasonCode: String = ""
)