package com.tokopedia.talk.feature.smartreply.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionGetSmartReplyResponseWrapper(
        @SerializedName("discussionGetSmartReply")
        @Expose
        val discussionGetSmartReply: DiscussionGetSmartReply = DiscussionGetSmartReply()
)