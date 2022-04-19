package com.tokopedia.topchat.chatroom.domain.pojo.srw


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatSmartReplyQuestion(
        @SerializedName("hasQuestion")
        @Expose
        var hasQuestion: Boolean = false,
        @SerializedName("list")
        @Expose
        var questions: List<QuestionUiModel> = listOf(),
        @SerializedName("title")
        @Expose
        var title: String = ""
)