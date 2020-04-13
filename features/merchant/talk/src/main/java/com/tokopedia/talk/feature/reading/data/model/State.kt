package com.tokopedia.talk.feature.reading.data.model

import com.google.gson.annotations.SerializedName

data class State(
        @SerializedName("isMasked")
        val isMasked: Boolean = false,
        @SerializedName("isLiked")
        val isLiked: Boolean = false,
        @SerializedName("allowLike")
        val allowLike: Boolean = false
)