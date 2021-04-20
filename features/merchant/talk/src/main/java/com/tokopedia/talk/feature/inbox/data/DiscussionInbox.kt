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
        @SerializedName("sellerUnread")
        @Expose
        val sellerUnread: Long = 0,
        @SerializedName("buyerUnread")
        @Expose
        val buyerUnread: Long = 0,
        @SerializedName("problemTotal")
        @Expose
        val problemTotal: Long = 0,
        @SerializedName("unrespondedTotal")
        @Expose
        val unrespondedTotal: Long = 0,
        @SerializedName("hasNext")
        @Expose
        val hasNext: Boolean = false,
        @SerializedName("inbox")
        @Expose
        val inbox: List<DiscussionInboxDetail> = emptyList()
)