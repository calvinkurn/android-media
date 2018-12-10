package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * @author : Steven 30/11/18
 */

data class ChatItemPojo(
        @Expose
        @SerializedName("msg_id")
        private var msgId: Int = 0,
        @Expose
        @SerializedName("user_id")
        private var userId: Int = 0,
        @Expose
        @SerializedName("reply_id")
        private var replyId: Int = 0,
        @Expose
        @SerializedName("sender_id")
        private var senderId: String? = null,
        @Expose
        @SerializedName("sender_name")
        private var senderName: String? = null,
        @Expose
        @SerializedName("role")
        private var role: String? = null,
        @Expose
        @SerializedName("msg")
        private var msg: String? = null,
        @Expose
        @SerializedName("reply_time")
        private var replyTime: String? = null,
        @Expose
        @SerializedName("reply_time_nano")
        private var replyTimeNano: String? = null,
        @Expose
        @SerializedName("fraud_status")
        private var fraudStatus: Int = 0,
        @Expose
        @SerializedName("read_time")
        private var readTime: String? = null,
        @Expose
        @SerializedName("attachment_id")
        private var attachmentId: Int = 0,
        @Expose
        @SerializedName("attachment")
//        private var attachment: AttachmentPojo? = null,
        private var attachment: Objects? = null,
        @Expose
        @SerializedName("old_msg_id")
        private var oldMsgId: Int = 0,
        @Expose
        @SerializedName("message_is_read")
        private var messageIsRead: Boolean = false,
        @Expose
        @SerializedName("is_opposite")
        private var isOpposite: Boolean = false,

        @Expose
        @SerializedName("is_highlight")
        private var isHighlight: Boolean = false,

        @Expose
        @SerializedName("old_msg_title")
        private var oldMessageTitle: String? = null,

        @Expose
        @SerializedName("show_rating")
        private var showRating: Boolean = false,

        @Expose
        @SerializedName("rating_status")

        private var ratingStatus: Int = 0,

        @Expose
        @SerializedName("blast_id")
        private val blastId: Int = 0
)