package com.tokopedia.chatbot.chatbot2.view.util

import com.tokopedia.chatbot.ChatbotConstant

object CheckDynamicAttachmentValidity {
    fun checkValidity(contentCode: Int?): Boolean {
        if (contentCode == null) {
            return false
        }
        return ChatbotConstant.ReplyBoxType.ALLOWED_DYNAMIC_ATTACHMENT_TYPE.contains(contentCode)
    }
}
