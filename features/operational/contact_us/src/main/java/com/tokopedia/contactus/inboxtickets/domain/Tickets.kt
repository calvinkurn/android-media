package com.tokopedia.contactus.inboxtickets.domain

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
data class Tickets(
    @SerializedName("total_comments")
        val totalComments: Int = 0,
    @SerializedName("comments")
        var comments: List<CommentsItem>? = null,
    @SerializedName("create_time")
        val createTime: String? = null,
    @SerializedName("subject")
        val subject: String? = null,
    @SerializedName("is_gandalf")
        val isIsGandalf: Boolean = false,
    @SerializedName("read_status")
        val readStatus: String? = null,
    @SerializedName("message")
        var message: String? = null,
    @SerializedName("created_by")
        val createdBy: CreatedBy? = null,
    @SerializedName("solution_id")
        val solutionId: Long = 0,
    @SerializedName("show_bad_CSAT_reason")
        val isShowBadCSATReason: Boolean = false,
    @SerializedName("show_rating")
        val isShowRating: Boolean = false,
    @SerializedName("number")
        val number: String? = null,
    @SerializedName("attachment")
        var attachment: List<AttachmentItem>? = null,
    @SuppressLint("Invalid Data Type") // can not use string or long since ids is a list
        @SerializedName("bad_rating_comment_id")
        val badRatingCommentId: List<String>? = null,
    @SerializedName("need_attachment")
        val isNeedAttachment: Boolean = false,
    @SerializedName("id")
        var id: String? = null,
    @SerializedName("invoice")
        var invoice: String? = null,
    @SerializedName("status")
        var status: String? = null
)
