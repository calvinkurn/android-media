package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AnswerState(
        @SerializedName("isMasked")
        @Expose
        val isMasked: Boolean = false,
        @SerializedName("isLiked")
        @Expose
        val isLiked: Boolean = false,
        @SerializedName("allowLike")
        @Expose
        val allowLike: Boolean = false,
        @SerializedName("allowUnmask")
        @Expose
        val allowUnmask: Boolean = false,
        @SerializedName("allowReport")
        @Expose
        val allowReport: Boolean = false,
        @SerializedName("allowDelete")
        @Expose
        val allowDelete: Boolean = false,
        @SerializedName("isYours")
        @Expose
        val isYours: Boolean = false,
        @SerializedName("isAutoReplied")
        @Expose
        val isAutoReplied: Boolean = false
)