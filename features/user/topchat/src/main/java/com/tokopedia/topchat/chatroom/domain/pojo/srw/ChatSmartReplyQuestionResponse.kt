package com.tokopedia.topchat.chatroom.domain.pojo.srw


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatSmartReplyQuestionResponse(
        @SerializedName("chatSmartReplyQuestion")
        @Expose
        var chatSmartReplyQuestion: ChatSmartReplyQuestion = ChatSmartReplyQuestion()
)