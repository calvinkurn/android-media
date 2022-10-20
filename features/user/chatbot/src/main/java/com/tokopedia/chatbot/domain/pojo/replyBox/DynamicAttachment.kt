package com.tokopedia.chatbot.domain.pojo.replyBox

import com.google.gson.annotations.SerializedName

data class DynamicAttachment(
    @SerializedName("dynamic_attachment")
    val dynamicAttachmentAttribute: DynamicAttachmentAttribute?
) {
    data class DynamicAttachmentAttribute(
        @SerializedName("attribute")
        val replyBoxAttribute: ReplyBoxAttribute?
    )
}
