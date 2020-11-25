package com.tokopedia.talk.feature.reply.data.model.createcomment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.common.data.TalkMutationData

data class TalkCreateNewComment(
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("messageError")
        @Expose
        val messageError: List<String> = listOf(),
        @SerializedName("data")
        @Expose
        val data: TalkMutationData = TalkMutationData(),
        @SerializedName("messageErrorOriginal")
        @Expose
        val originalErrorMessage: List<String> = listOf()
)