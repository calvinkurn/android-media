package com.tokopedia.contactus.inboxtickets.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.contactus.inboxtickets.domain.AttachmentItem
import com.tokopedia.contactus.inboxtickets.domain.CommentsItem
import com.tokopedia.contactus.inboxtickets.domain.CreatedBy
import com.tokopedia.csat_rating.data.BadCsatReasonListItem

class Tickets {
    @SerializedName("badCsatReasonList")
    var badCsatReasonList: ArrayList<BadCsatReasonListItem>? = null

    @SerializedName("showRating")
    var isShowRating = false

    @SerializedName("id")
    var id: String? = null

    @SerializedName("comments")
    var comments: MutableList<CommentsItem>? = null

    @SerializedName("createTime")
    val createTime: String? = null

    @SerializedName("subject")
    val subject: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("createdBy")
    val createdBy: CreatedBy? = null

    @SerializedName("number")
    var number: String? = null

    @SerializedName("attachment")
    var attachment: List<AttachmentItem>? = null

    @SuppressLint("Invalid Data Type")
    // can not use string or long since ids is a list
    @SerializedName("bad_rating_comment_id")
    val badRatingCommentId: List<String>? = null

    @SerializedName("need_attachment")
    var isNeedAttachment = false

    @SerializedName("invoice")
    var invoice: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("allowClose")
    var isAllowClose = false

    fun isCommentsNull() = comments == null

    fun getTicketComment() = comments ?: arrayListOf()

    fun getBadCsatReasons() = badCsatReasonList ?: arrayListOf()

    fun getMessageTicket() = message.orEmpty()

    fun getCreatedTicketBy() = createdBy ?: CreatedBy()

    fun getCreateTimeTicket() = createTime.orEmpty()

    fun getTicketNumber() = number.orEmpty()

    fun getTicketId() = id.orEmpty()

    fun getStatusTicket() = status.orEmpty()

    override fun toString(): String {
        return "Tickets{" +
            "badCsatReasonList = '" + badCsatReasonList + '\'' +
            ",showRating = '" + isShowRating + '\'' +
            ",id = '" + id + '\'' +
            "}"
    }
}
