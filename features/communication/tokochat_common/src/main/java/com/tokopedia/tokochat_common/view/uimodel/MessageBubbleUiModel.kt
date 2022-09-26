package com.tokopedia.tokochat_common.view.uimodel

data class MessageBubbleUiModel (
    var sentAt: Long = 0,
    var isRead: Boolean = false,
    var isDummy: Boolean = false,
    var isSender: Boolean = false,
    var type: String = ""
)
