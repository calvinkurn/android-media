package com.tokopedia.chatbot.view.uimodel

import androidx.annotation.DrawableRes
import com.tokopedia.iconunify.IconUnify

sealed class ChatbotReplyOptionsUiModel(
    open val title: String,
    @DrawableRes
    open val icon: Int? = null
) {
    data class CopyToClipboard(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.COPY
    ): ChatbotReplyOptionsUiModel(title,icon)

    data class Reply(
        override val title: String,
        @DrawableRes
        override val icon: Int = IconUnify.REPLY
    ): ChatbotReplyOptionsUiModel(title,icon)
}
