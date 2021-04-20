package com.tokopedia.talk.feature.inbox.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionInboxResponseWrapper(
        @SerializedName("discussionInbox")
        @Expose
        val discussionInbox: DiscussionInbox = DiscussionInbox()
)