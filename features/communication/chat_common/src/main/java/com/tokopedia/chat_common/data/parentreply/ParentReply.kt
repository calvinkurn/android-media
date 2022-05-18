package com.tokopedia.chat_common.data.parentreply

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero

data class ParentReply(
    @SerializedName("attachmentID", alternate = ["attachment_id"])
    val attachmentId: String = "",
    @SerializedName("attachmentType", alternate = ["attachment_type"])
    val attachmentType: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("senderID", alternate = ["sender_id"])
    val senderId: String = "",
    @SerializedName("replyTimeUnixNano", alternate = ["reply_time"])
    val replyTime: String = "",
    @SerializedName("fraudStatus", alternate = ["fraud_status"])
    val fraudStatus: Int = 0,
    @SerializedName("mainText", alternate = ["main_text"])
    val mainText: String = "",
    @SerializedName("subText", alternate = ["sub_text"])
    val subText: String = "",
    @SerializedName("imageURL", alternate = ["image_url"])
    val imageUrl: String = "",
    @SerializedName("localID", alternate = ["local_id"])
    val localId: String = "",
    @SerializedName("isExpired", alternate = ["is_expired"])
    val isExpired: Boolean = false,
    @SerializedName("source")
    val source: String = "",
    @SerializedName("replyID")
    val replyId: String = "",
) {

    val replyTimeMillisOffset: String get() {
        val addOffsetTimeStamp = (replyTime.toLongOrZero() / 1_000_000)
        return addOffsetTimeStamp.toString()
    }

}