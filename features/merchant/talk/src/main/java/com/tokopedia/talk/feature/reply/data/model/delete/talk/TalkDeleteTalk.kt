package com.tokopedia.talk.feature.reply.data.model.delete.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkDeleteTalk(
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("messageError")
        @Expose
        val messageError: List<String> = listOf(),
        @SerializedName("data")
        @Expose
        val data: TalkDeleteTalkResultData = TalkDeleteTalkResultData(),
        @SerializedName("messageErrorOriginal")
        @Expose
        val originalErrorMessage: List<String> = listOf()
)