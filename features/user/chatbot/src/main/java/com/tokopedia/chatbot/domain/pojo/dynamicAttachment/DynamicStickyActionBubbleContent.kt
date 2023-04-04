package com.tokopedia.chatbot.domain.pojo.dynamicAttachment

data class DynamicStickyActionBubbleContent(
    val action: String,
    val text: String,
    val value: String
)
data class DynamicButtonAction(
    val content: DynamicStickyActionBubbleContent
)
