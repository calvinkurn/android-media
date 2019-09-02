package com.tokopedia.chatbot.domain.pojo.livechatdivider

import com.google.gson.annotations.SerializedName

data class LiveChatDividerResponse(

        @SerializedName("is_opposite")
        val isOpposite: Boolean = false,

        @SerializedName("thumbnail")
        val thumbnail: String,

        @SerializedName("from_role")
        val fromRole: String?,

        @SerializedName("message")
        val message: Message?,

        @SerializedName("from_uid")
        val fromUid: Int = 0,

        @SerializedName("show_rating")
        val showRating: Boolean = false,

        @SerializedName("start_time")
        val startTime: String?,

        @SerializedName("from_user_name")
        val fromUserName: String?,

        @SerializedName("rating_status")
        val ratingStatus: Int = 0,

        @SerializedName("attachment")
        val attachment: Attachment?,

        @SerializedName("client_connect_time")
        val clientConnectTime: String?,

        @SerializedName("to_uid")
        val toUid: Int = 0,

        @SerializedName("attachment_id")
        val attachmentId: Int = 0,

        @SerializedName("from")
        val from: String?,

        @SerializedName("to_buyer")
        val toBuyer: Boolean = false,

        @SerializedName("msg_id")
        val msgId: Int = 0,

        @SerializedName("is_bot")
        val isBot: Boolean = false,

        @SerializedName("notifications")
        val notifications: List<NotificationsItem?>?
)