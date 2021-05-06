package com.tokopedia.talk.feature.sellersettings.smartreply.settings.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionGetSmartReply(
        @SerializedName("isSmartReplyOn")
        @Expose
        val isSmartReplyOn: Boolean = false,
        @SerializedName("totalQuestion")
        @Expose
        val totalQuestion: String = "",
        @SerializedName("totalAnsweredBySmartReply")
        @Expose
        val totalAnsweredBySmartReply: String = "",
        @SerializedName("replySpeed")
        @Expose
        val replySpeed: String = "",
        @SerializedName("messageReady")
        @Expose
        val messageReady: String = "",
        @SerializedName("messageNotReady")
        @Expose
        val messageNotReady: String = ""
)