package com.tokopedia.tokochat.common.view.chatroom.uimodel

import androidx.annotation.DrawableRes

data class TokoChatAttachmentMenuUiModel (
    val title: String,
    @DrawableRes val icon: Int,
    val type: TokoChatAttachmentType
)

enum class TokoChatAttachmentType {
    IMAGE_ATTACHMENT
}
