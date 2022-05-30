package com.tokopedia.topchat.chatlist.domain.pojo.reply

import com.google.gson.annotations.SerializedName

class WebSocketResponseData {
    @SerializedName("msg_id")
    val msgId: String = ""

    @SerializedName("from_uid")
    val fromUid: String = ""

    @SerializedName("from")
    var from: String = ""

    @SerializedName("from_role")
    var fromRole: String = ""

    @SerializedName("to_uid")
    val toUid: String = ""

    @SerializedName("message")
    var message: Message = Message()

    @SerializedName("start_time")
    var startTime: String = ""

    @SerializedName("thumbnail")
    var imageUri: String = ""

    @SerializedName("attachment")
    var attachment: Attachment? = Attachment()

    @SerializedName("show_rating")
    var isShowRating: Boolean = false

    @SerializedName("rating_status")
    var ratingStatus: Int = 0

    @SerializedName("is_bot")
    val isBot: Boolean = false

    @SerializedName("is_opposite")
    var isOpposite: Boolean = false

    @SerializedName("is_auto_reply")
    val isAutoReply: Boolean = false
}