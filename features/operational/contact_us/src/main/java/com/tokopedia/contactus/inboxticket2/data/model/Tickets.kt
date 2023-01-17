package com.tokopedia.contactus.inboxticket2.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem
import com.tokopedia.contactus.inboxticket2.domain.CommentsItem
import com.tokopedia.contactus.inboxticket2.domain.CreatedBy
import com.tokopedia.csat_rating.data.BadCsatReasonListItem

class Tickets {
    @SerializedName("badCsatReasonList")
    val badCsatReasonList: ArrayList<BadCsatReasonListItem>? = null
    @SerializedName("showRating")
    var isShowRating = false
    @SerializedName("id")
    var id: String? = null
    @SerializedName("total_comments")
    private val totalComments = 0
	@SerializedName("comments")
    var comments: MutableList<CommentsItem>? = null
    @SerializedName("createTime")
    val createTime: String? = null
	@SerializedName("subject")
    val subject: String? = null
    @SerializedName("is_gandalf")
    val isIsGandalf = false
    @SerializedName("read_status")
    val readStatus: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("createdBy")
    val createdBy: CreatedBy? = null
    @SerializedName("solution_id")
    val solutionId: Long = 0
    @SerializedName("show_bad_CSAT_reason")
    val isShowBadCSATReason = false
	@SerializedName("number")
    val number: String? = null
    @SerializedName("attachment")
    var attachment: List<AttachmentItem>? = null
    @SuppressLint("Invalid Data Type") // can not use string or long since ids is a list
    @SerializedName("bad_rating_comment_id")
    val badRatingCommentId: List<String>? = null
    @SerializedName("need_attachment")
    var isNeedAttachment = false
    @SerializedName("invoice")
    var invoice: String? = null
	@SerializedName("status")
    var status: String? = null
    @SerializedName("allowClose")
    val isAllowClose = false

    override fun toString(): String {
        return "Tickets{" +
                "badCsatReasonList = '" + badCsatReasonList + '\'' +
                ",showRating = '" + isShowRating + '\'' +
                ",id = '" + id + '\'' +
                "}"
    }
}
