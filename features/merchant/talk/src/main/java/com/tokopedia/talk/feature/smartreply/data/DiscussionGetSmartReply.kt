package com.tokopedia.talk.feature.smartreply.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionGetSmartReply(
        @SerializedName("isSmartReplyOn")
        @Expose
        val isSmartReplyOn: Boolean = false,
        @SerializedName("totalQuestion")
        @Expose
        val totalQuestion: Int = 0,
        @SerializedName("totalAnsweredBySmartReply")
        @Expose
        val totalAnsweredBySmartReply: Int = 3,
        @SerializedName("replySpeed")
        @Expose
        val replySpeed: Int = 0,
        @SerializedName("messageReady")
        @Expose
        val messageReady: String = "",
        @SerializedName("messageNotReady")
        @Expose
        val messageNotReady: String = ""
)