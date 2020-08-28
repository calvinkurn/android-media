package com.tokopedia.talk.feature.inbox.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionInbox(
        @SerializedName("userName")
        @Expose
        val userName: String = "",
        @SerializedName("shopID")
        @Expose
        val shopID: String = "",
        @SerializedName("")
        @Expose
        val shopName: String = "",
        @SerializedName("inboxType")
        @Expose
        val inboxType: String = "",
        @SerializedName("hasNext")
        @Expose
        val hasNext: Boolean = false,
        @SerializedName("inbox")
        @Expose
        val inbox: List<DiscussionInboxDetail> = emptyList()
)