package com.tokopedia.chatbot.chatbot2.data.dynamicAttachment

import com.google.gson.annotations.SerializedName

data class DynamicAttachment(
    @SerializedName("dynamic_attachment", alternate = ["dynamic_attachment_payload"])
    val dynamicAttachmentAttribute: DynamicAttachmentAttribute?
) {
    data class DynamicAttachmentAttribute(
        @SerializedName("attribute")
        val dynamicAttachmentBodyAttributes: DynamicAttachmentBodyAttributes?,
        @SerializedName("fallback")
        val dynamicAttachmentFallback: DynamicAttachmentFallback,
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
