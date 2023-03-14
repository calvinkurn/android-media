package com.tokopedia.chatbot.domain.pojo.dynamicattachment

data class DynamicStickyActionBubbleContent(
    val action: String,
    val text: String,
    val value: String
)
data class DynamicButtonAction(
    val content: DynamicStickyActionBubbleContent
)
