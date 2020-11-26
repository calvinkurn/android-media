package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuestionState(
        @SerializedName("allowReply")
        @Expose
        val allowReply: Boolean = false,
        @SerializedName("allowUnmask")
        @Expose
        val allowUnmask: Boolean = false,
        @SerializedName("allowReport")
        @Expose
        val allowReport: Boolean = false,
        @SerializedName("allowDelete")
        @Expose
        val allowDelete: Boolean = false,
        @SerializedName("allowFollow")
        @Expose
        val allowFollow: Boolean = false,
        @SerializedName("isFollowed")
        @Expose
        val isFollowed: Boolean = false,
        @SerializedName("isMasked")
        @Expose
        val isMasked: Boolean = false,
        @SerializedName("isYours")
        @Expose
        val isYours: Boolean = false
)