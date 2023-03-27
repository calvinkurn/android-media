package com.tokopedia.chatbot.domain.pojo.dynamicAttachment

import com.google.gson.annotations.SerializedName

data class DynamicAttachment(
    @SerializedName("dynamic_attachment", alternate = ["dynamic_attachment_payload"])
    val dynamicAttachmentAttribute: DynamicAttachmentAttribute?
) {
    data class DynamicAttachmentAttribute(
        @SerializedName("attribute")
        val dynamicAttachmentBodyAttributes: com.tokopedia.chatbot.domain.pojo.dynamicAttachment.DynamicAttachmentBodyAttributes?,
        @SerializedName("fallback")
        val dynamicAttachmentFallback: DynamicAttachmentFallback?,
        @SerializedName("is_log_history")
        val isLogHistory: Boolean?
    ) {
        data class DynamicAttachmentFallback(
            @SerializedName("html")
            val html: String?,
            @SerializedName("message")
            val fallbackMessage: String?
        )
    }
}
