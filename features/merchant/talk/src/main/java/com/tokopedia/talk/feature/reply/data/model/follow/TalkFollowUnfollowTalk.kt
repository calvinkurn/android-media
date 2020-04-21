package com.tokopedia.talk.feature.reply.data.model.follow

import com.google.gson.annotations.SerializedName

data class TalkFollowUnfollowTalk(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("messageError")
        val messageError: List<String> = listOf(),
        @SerializedName("data")
        val data: TalkFollowUnfollowTalkResultData = TalkFollowUnfollowTalkResultData(),
        @SerializedName("messageErrorOriginal")
        val originalErrorMessage: List<String> = listOf()
)