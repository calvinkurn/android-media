package com.tokopedia.chat_common.domain.pojo

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.tickerreminder.TickerReminderPojo
import com.tokopedia.chat_common.util.IdentifierUtil

/**
 * @author : Steven 06/12/18
 */

data class ChatSocketPojo(
    @SerializedName("msg_id")
    var msgId: String = "0",

    @SerializedName("from_uid")
    var fromUid: String = "",

    @SerializedName("from")
    var from: String = "",

    @SerializedName("from_role")
    var fromRole: String = "",

    @SerializedName("to_uid")
    var toUid: String = "0",

    @SerializedName("message")
    var message: MessagePojo = MessagePojo(),

    @SerializedName("start_time")
    var startTime: String = "",

    @SerializedName("thumbnail")
    var imageUri: String = "",

    @SerializedName("attachment")
    var attachment: AttachmentPojo? = null,

    @SerializedName("show_rating")
    var showRating: Boolean = false,

    @SerializedName("rating_status")
    var ratingStatus: Int = 0,

    @SerializedName("is_opposite")
    var isOpposite: Boolean = false,

    @SerializedName("blast_id")
    var blastId: String = "0",

    @SerializedName("source")
    var source: String = "",

    @SerializedName("label")
    var label: String = "",

    @SerializedName("local_id")
    var localId: String = "",

    @SerializedName("parent_reply")
    val parentReply: ParentReply? = null,

    @SerializedName("fraud_status")
    val fraudStatus: Int = 0,

    @SerializedName("reply_time_nano")
    val replyTime: String = "",

    @SerializedName("reminder_ticker")
    var tickerReminder: TickerReminderPojo? = null
) {

    fun generateLocalIdIfNotExist() {
        if (localId.isEmpty()) {
            localId = IdentifierUtil.generateLocalId()
        }
    }

}


data class AttachmentPojo(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("attributes")
    val attributes: JsonObject? = null,

    @SerializedName("fallback_attachment")
    val fallbackAttachment: Fallback = Fallback()
)
