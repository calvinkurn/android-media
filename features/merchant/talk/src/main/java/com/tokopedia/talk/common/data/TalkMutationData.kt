package com.tokopedia.talk.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkMutationData(
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Int = 0,
        @SerializedName("talkID")
        @Expose
        val talkId: Int = 0
)