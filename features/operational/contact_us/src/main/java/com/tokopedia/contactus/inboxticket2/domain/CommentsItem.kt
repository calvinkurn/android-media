package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

data class CommentsItem(
        @SerializedName("note")
        val note: String? = null,
        @SerializedName("create_time")
        var createTime: String? = null,
        @SerializedName("attachment")
        var attachment: List<AttachmentItem>? = null,
        @SerializedName("message_plaintext")
        var messagePlaintext: String? = null,
        @SerializedName("rating")
        var rating: String? = null,
        @SerializedName("id")
        var id: String? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("created_by")
        var createdBy: CreatedBy? = null,
        var shortTime: String? = null,
        var isCollapsed: Boolean = true,
        var priorityLabel: Boolean = false,
        var ticketTitle: String? = null,
        var ticketId: String? = null,
        var ticketStatus: String? = null
)
