package com.tokopedia.talk.feature.reply.data.model.delete.comment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkDeleteComment(
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("messageError")
        @Expose
        val messageError: List<String> = listOf(),
        @SerializedName("data")
        @Expose
        val data: TalkDeleteCommentResultData = TalkDeleteCommentResultData(),
        @SerializedName("messageErrorOriginal")
        @Expose
        val originalErrorMessage: List<String> = listOf()
)