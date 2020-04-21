package com.tokopedia.talk.feature.reply.data.model.createcomment

import com.google.gson.annotations.SerializedName

data class TalkCreateNewComment(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("messageError")
        val messageError: List<String> = listOf(),
        @SerializedName("data")
        val data: TalkCreateNewCommentResultData = TalkCreateNewCommentResultData(),
        @SerializedName("messageErrorOriginal")
        val originalErrorMessage: List<String> = listOf()
)