package com.tokopedia.talk.feature.reply.data.model.follow

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkFollowUnfollowTalk(
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("messageError")
        @Expose
        val messageError: List<String> = listOf(),
        @SerializedName("data")
        @Expose
        val data: TalkFollowUnfollowTalkResultData = TalkFollowUnfollowTalkResultData(),
        @SerializedName("messageErrorOriginal")
        @Expose
        val originalErrorMessage: List<String> = listOf()
)