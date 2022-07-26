package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 30/11/18
 */

data class ChatItemPojo(
        @SerializedName("msg_id")
        var msgId: String = "0",

        @SerializedName("user_id")
        var userId: String = "0",

        @SerializedName("sender_id")
        var senderId: String="",

        @SerializedName("from")
        var senderName: String="",

        @SerializedName("role")
        var role: String="",

        @SerializedName("msg")
        var msg: String="",

        @SerializedName("reply_time")
        var replyTime: String="",

        @SerializedName("reply_time_nano")
        var replyTimeNano: String="",

        @SerializedName("fraud_status")
        var fraudStatus: Int = 0,

        @SerializedName("read_time")
        var readTime: String="",

        @SerializedName("attachment_id")
        var attachmentId: String = "0",

        @SerializedName("attachment")
        var attachment: Attachment?,

        @SerializedName("old_msg_id")
        var oldMsgId: String = "0",

        @SerializedName("message_is_read")
        var messageIsRead: Boolean = false,

        @SerializedName("is_opposite")
        var isOpposite: Boolean = false,

        @SerializedName("is_highlight")
        var isHighlight: Boolean = false,

        @SerializedName("old_msg_title")
        var oldMessageTitle: String="",

        @SerializedName("show_rating")
        var showRating: Boolean = false,

        @SerializedName("rating_status")
        var ratingStatus: Int = 0,

        @SerializedName("blast_id")
        val blastId: String = "0",

        @SerializedName("source")
        val source: String? = ""
)