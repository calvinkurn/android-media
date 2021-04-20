package com.tokopedia.talk.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkMutationResponse(
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