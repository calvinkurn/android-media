package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 30/11/18
 */

data class ChatItemPojo(
        @Expose
        @SerializedName("msg_id")
        var msgId: Long = 0L,
        @Expose
        @SerializedName("user_id")
        var userId: Long = 0L,
        @Expose
        @SerializedName("sender_id")
        var senderId: String="",
        @Expose
        @SerializedName("from")
        var senderName: String="",
        @Expose
        @SerializedName("role")
        var role: String="",
        @Expose
        @SerializedName("msg")
        var msg: String="",
        @Expose
        @SerializedName("reply_time")
        var replyTime: String="",
        @Expose
        @SerializedName("reply_time_nano")
        var replyTimeNano: String="",
        @Expose
        @SerializedName("fraud_status")
        var fraudStatus: Int = 0,
        @Expose
        @SerializedName("read_time")
        var readTime: String="",
        @Expose
        @SerializedName("attachment_id")
        var attachmentId: Long = 0L,
        @Expose
        @SerializedName("attachment")
        var attachment: Attachment?,
        @Expose
        @SerializedName("old_msg_id")
        var oldMsgId: Long = 0L,
        @Expose
        @SerializedName("message_is_read")
        var messageIsRead: Boolean = false,
        @Expose
        @SerializedName("is_opposite")
        var isOpposite: Boolean = false,

        @Expose
        @SerializedName("is_highlight")
        var isHighlight: Boolean = false,

        @Expose
        @SerializedName("old_msg_title")
        var oldMessageTitle: String="",

        @Expose
        @SerializedName("show_rating")
        var showRating: Boolean = false,

        @Expose
        @SerializedName("rating_status")

        var ratingStatus: Int = 0,

        @Expose
        @SerializedName("blast_id")
        val blastId: Long = 0L,

        @Expose
        @SerializedName("source")
        val source: String? = ""
)