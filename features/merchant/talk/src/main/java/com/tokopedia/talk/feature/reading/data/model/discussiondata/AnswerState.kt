package com.tokopedia.talk.feature.reading.data.model.discussiondata

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
        @SerializedName("isYours")
        @Expose
        val isYours: Boolean = false
)