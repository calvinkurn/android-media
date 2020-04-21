package com.tokopedia.talk.feature.reply.data.model

import com.google.gson.annotations.SerializedName

data class TalkFollowUnfollowTalkResultData(
        @SerializedName("isSuccess")
        val isSuccess: Int = 0,
        @SerializedName("talkID")
        val talkId: Int = 0
)