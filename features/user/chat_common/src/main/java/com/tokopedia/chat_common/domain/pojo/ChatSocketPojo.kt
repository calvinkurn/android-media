package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 06/12/18
 */

data class ChatSocketPojo(
        @SerializedName("msg_id")
        @Expose
        var msgId: Int = 0,
        @SerializedName("from_uid")
        @Expose
        var fromUid: String = "",
        @SerializedName("from")
        @Expose
        var from: String = "",
        @SerializedName("from_role")
        @Expose
        var fromRole: String = "",
        @SerializedName("to_uid")
        @Expose
        var toUid: Int = 0,
        @SerializedName("message")
        @Expose
        var message: MessagePojo,
        @SerializedName("start_time")
        @Expose
        var startTime: String = "",
        @SerializedName("thumbnail")
        @Expose
        var imageUri: String = "",
        @SerializedName("attachment")
        @Expose
        var attachment: AttachmentPojo,
        @SerializedName("show_rating")
        @Expose
        var showRating: Boolean = false,
        @SerializedName("rating_status")
        @Expose
        var ratingStatus: Int = 0,
        @SerializedName("is_opposite")
        @Expose
        var isOpposite: Boolean = false
)