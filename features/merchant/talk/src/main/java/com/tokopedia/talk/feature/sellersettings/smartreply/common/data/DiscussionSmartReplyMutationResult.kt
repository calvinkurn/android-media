package com.tokopedia.talk.feature.sellersettings.smartreply.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionSmartReplyMutationResult(
        @SerializedName("isSuccess")
        @Expose
        val isSuccess: Boolean = false,
        @SerializedName("reason")
        @Expose
        val reason: String = ""
)