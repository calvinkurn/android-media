package com.tokopedia.talk.feature.write.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.talk.common.data.TalkMutationData

data class TalkCreateNewTalk(
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("messageError")
        @Expose
        val messageError: List<String> = emptyList(),
        @SerializedName("data")
        @Expose
        val talkMutationData: TalkMutationData = TalkMutationData(),
        @SerializedName("messageErrorOriginal")
        @Expose
        val messageErrorOriginal: List<String> = emptyList()
)