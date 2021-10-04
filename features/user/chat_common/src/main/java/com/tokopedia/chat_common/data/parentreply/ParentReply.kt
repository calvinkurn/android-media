package com.tokopedia.chat_common.data.parentreply

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toLongOrZero

data class ParentReply(
    @SerializedName("attachment_id")
    val attachmentId: String = "",
    @SerializedName("attachment_type")
    val attachmentType: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("sender_id")
    val senderId: String = "",
    @SerializedName("reply_time")
    val replyTime: String = "",
    @SerializedName("fraud_status")
    val fraudStatus: Int = 0,
    @SerializedName("main_text")
    val mainText: String = "",
    @SerializedName("sub_text")
    val subText: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("local_id")
    val localId: String = "",
    @SerializedName("is_expired")
    val isExpired: Boolean = false,
    @SerializedName("source")
    val source: String = "",
) {

    val replyTimeMillisOffset: String get() {
        val addOffsetTimeStamp = (replyTime.toLongOrZero() / 1_000_000) + 5000
        return addOffsetTimeStamp.toString()
    }

}