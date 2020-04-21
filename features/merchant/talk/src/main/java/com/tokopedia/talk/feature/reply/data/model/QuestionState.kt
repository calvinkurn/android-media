package com.tokopedia.talk.feature.reply.data.model

import com.google.gson.annotations.SerializedName

data class QuestionState(
        @SerializedName("allowReply")
        val allowReply: Boolean = false,
        @SerializedName("allowUnmask")
        val allowUnmask: Boolean = false,
        @SerializedName("allowReport")
        val allowReport: Boolean = false,
        @SerializedName("allowDelete")
        val allowDelete: Boolean = false,
        @SerializedName("allowFollow")
        val allowFollow: Boolean = false,
        @SerializedName("isFollowed")
        val isFollowed: Boolean = false,
        @SerializedName("isMasked")
        val isMasked: Boolean = false
)