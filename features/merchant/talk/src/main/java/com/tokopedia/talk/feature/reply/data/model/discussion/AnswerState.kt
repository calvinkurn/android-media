package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.SerializedName

data class AnswerState(
        @SerializedName("isMasked")
        val isMasked: Boolean = false,
        @SerializedName("isLiked")
        val isLiked: Boolean = false,
        @SerializedName("allowLike")
        val allowLike: Boolean = false,
        @SerializedName("allowUnmask")
        val allowUnmask: Boolean = false,
        @SerializedName("allowReport")
        val allowReport: Boolean = false,
        @SerializedName("allowDelete")
        val allowDelete: Boolean = false
)