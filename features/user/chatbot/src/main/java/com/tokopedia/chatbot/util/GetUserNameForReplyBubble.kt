package com.tokopedia.chatbot.util

import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetUserNameForReplyBubble @Inject constructor(
    private val userSession : UserSessionInterface
){
    fun getUserName(userId : String?) : String {
        if (userId == userSession.userId)
            return userSession.name
        return ChatbotConstant.TOKOPEDIA_CARE
    }

}