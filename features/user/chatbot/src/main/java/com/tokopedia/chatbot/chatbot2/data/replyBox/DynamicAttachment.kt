package com.tokopedia.chatbot.chatbot2.domain.pojo.replyBox

import com.google.gson.annotations.SerializedName

data class DynamicAttachment(
    @SerializedName("dynamic_attachment", alternate = ["dynamic_attachment_payload"])
    val dynamicAttachmentAttribute: DynamicAttachmentAttribute?
) {
    data class DynamicAttachmentAttribute(
        @SerializedName("attribute")
        val replyBoxAttribute: ReplyBoxAttribute?
    )
}
