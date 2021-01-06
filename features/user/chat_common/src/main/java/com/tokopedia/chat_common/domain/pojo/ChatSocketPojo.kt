package com.tokopedia.chat_common.domain.pojo

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 06/12/18
 */

data class ChatSocketPojo(
        @SerializedName("msg_id")
        @Expose
        var msgId: Long = 0L,
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
        var toUid: Long = 0L,
        @SerializedName("message")
        @Expose
        var message: MessagePojo = MessagePojo(),
        @SerializedName("start_time")
        @Expose
        var startTime: String = "",
        @SerializedName("thumbnail")
        @Expose
        var imageUri: String = "",
        @SerializedName("attachment")
        @Expose
        var attachment: AttachmentPojo? = null,
        @SerializedName("show_rating")
        @Expose
        var showRating: Boolean = false,
        @SerializedName("rating_status")
        @Expose
        var ratingStatus: Int = 0,
        @SerializedName("is_opposite")
        @Expose
        var isOpposite: Boolean = false,
        @SerializedName("blast_id")
        @Expose
        var blastId: Long = 0L,
        @SerializedName("source")
        @Expose
        var source: String = ""
)


data class AttachmentPojo(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: JsonObject? = null,
        @SerializedName("fallback_attachment")
        @Expose
        val fallbackAttachment: Fallback = Fallback()
)
