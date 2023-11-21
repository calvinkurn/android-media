package com.tokopedia.chatbot.chatbot2.data.newchatbotsession

import com.google.gson.annotations.SerializedName

data class DynamicAttachmentNewChatbotSession(
    @SerializedName("is_new_chatbot_session")
    val isNewChatbotSession: Boolean
)
