package com.tokopedia.talk.feature.reply.data.model.delete.talk

import com.google.gson.annotations.SerializedName

data class TalkDeleteTalk(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("messageError")
        val messageError: List<String> = listOf(),
        @SerializedName("data")
        val data: TalkDeleteTalkResultData = TalkDeleteTalkResultData(),
        @SerializedName("messageErrorOriginal")
        val originalErrorMessage: List<String> = listOf()
)