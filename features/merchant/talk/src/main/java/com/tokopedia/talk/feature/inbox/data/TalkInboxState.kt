package com.tokopedia.talk.feature.inbox.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalkInboxState(
        @SerializedName("isUnresponded")
        @Expose
        val isUnresponded: Boolean = false,
        @SerializedName("hasProblem")
        @Expose
        val hasProblem: Boolean = false
)