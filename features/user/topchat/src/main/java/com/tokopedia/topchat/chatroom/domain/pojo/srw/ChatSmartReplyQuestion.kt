package com.tokopedia.topchat.chatroom.domain.pojo.srw


import com.google.gson.annotations.SerializedName

data class ChatSmartReplyQuestion(
        @SerializedName("hasQuestion")
        var hasQuestion: Boolean = false,
        @SerializedName("isSuccess")
        var isSuccess: Boolean = false,
        @SerializedName("list")
        var questions: List<QuestionUiModel> = listOf(),
        @SerializedName("title")
        var title: String = ""
)