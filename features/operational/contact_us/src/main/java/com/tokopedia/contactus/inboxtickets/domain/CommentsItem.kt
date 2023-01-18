package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.SerializedName

data class CommentsItem(
    @SerializedName("note")
    val note: String? = null,
    @SerializedName("create_time")
    var createTime: String? = null,
    @SerializedName("attachment")
    var attachment: List<AttachmentItem>? = null,
    @SerializedName("message_plaintext")
    var messagePlaintext: String = "",
    @SerializedName("rating")
    var rating: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("created_by")
    var createdBy: CreatedBy = CreatedBy(),
    var shortTime: String? = null,
    var isCollapsed: Boolean = true,
    var priorityLabel: Boolean = false,
    var ticketTitle: String? = null,
    var ticketId: String? = null,
    var ticketStatus: String? = null
){
    fun getCreateCommentTime() = createTime?: ""
}
