package com.tokopedia.talk.feature.reply.data.model

import com.google.gson.annotations.SerializedName

data class TalkFollowUnfollowTalkResultData(
        @SerializedName("isSucess")
        val isSuccess: Boolean = false,
        @SerializedName("talkID")
        val talkId: Int = 0
)