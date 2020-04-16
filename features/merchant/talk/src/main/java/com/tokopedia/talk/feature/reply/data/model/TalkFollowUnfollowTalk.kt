package com.tokopedia.talk.feature.reply.data.model

import com.google.gson.annotations.SerializedName

data class TalkFollowUnfollowTalk(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("messageError")
        val messageError: String = "",
        @SerializedName("data")
        val data: TalkFollowUnfollowTalkResultData = TalkFollowUnfollowTalkResultData(),
        @SerializedName("messageErrorOriginal")
        val originalErrorMessage: String = ""
)