package com.tokopedia.talk.feature.inbox.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionInboxDetail(
        @SerializedName("inboxID")
        @Expose
        val inboxID: String = "",
        @SerializedName("questionID")
        @Expose
        val questionID: String = "",
        @SerializedName("content")
        @Expose
        val content: String = "",
        @SerializedName("lastReplyTime")
        @Expose
        val lastReplyTime: String = "",
        @SerializedName("totalAnswer")
        @Expose
        val totalAnswer: Int = 0,
        @SerializedName("isUnread")
        @Expose
        val isUnread: Boolean = false,
        @SerializedName("isMasked")
        @Expose
        val isMasked: Boolean = false,
        @SerializedName("answererThumbnail")
        @Expose
        val respondentThumbnails: List<String>,
        @SerializedName("productID")
        @Expose
        val productID: String = "",
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("productThumbnail")
        @Expose
        val productThumbnail: String = "",
        @SerializedName("productURL")
        @Expose
        val productUrl: String = "",
        @SerializedName("state")
        @Expose
        val state: TalkInboxState = TalkInboxState()
)