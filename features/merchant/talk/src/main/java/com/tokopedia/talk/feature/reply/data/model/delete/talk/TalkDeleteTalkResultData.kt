package com.tokopedia.talk.feature.reply.data.model.delete.talk

import com.google.gson.annotations.SerializedName

data class TalkDeleteTalkResultData(
        @SerializedName("isSuccess")
        val isSuccess: Int = 0,
        @SerializedName("talkID")
        val talkId: Int = 0
)