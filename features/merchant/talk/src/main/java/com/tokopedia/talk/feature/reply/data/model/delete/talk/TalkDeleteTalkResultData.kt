package com.tokopedia.talk.feature.reply.data.model.delete.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkDeleteTalkResultData(
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Int = 0,
        @SerializedName("talkID")
        @Expose
        val talkId: Int = 0
)