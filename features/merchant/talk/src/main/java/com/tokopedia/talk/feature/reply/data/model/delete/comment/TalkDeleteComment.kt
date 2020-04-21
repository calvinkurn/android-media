package com.tokopedia.talk.feature.reply.data.model.delete.comment

import com.google.gson.annotations.SerializedName

data class TalkDeleteComment(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("messageError")
        val messageError: List<String> = listOf(),
        @SerializedName("data")
        val data: TalkDeleteCommentResultData = TalkDeleteCommentResultData(),
        @SerializedName("messageErrorOriginal")
        val originalErrorMessage: List<String> = listOf()
)