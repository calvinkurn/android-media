package com.tokopedia.topchat.chatroom.domain.pojo.srw


import com.google.gson.annotations.SerializedName

data class ChatSmartReplyQuestionResponse(
        @SerializedName("chatSmartReplyQuestion")
        var chatSmartReplyQuestion: ChatSmartReplyQuestion = ChatSmartReplyQuestion()
)