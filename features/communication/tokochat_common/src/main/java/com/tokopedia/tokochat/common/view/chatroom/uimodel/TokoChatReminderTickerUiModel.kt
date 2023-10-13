package com.tokopedia.tokochat.common.view.chatroom.uimodel

import com.tokopedia.kotlin.model.ImpressHolder

data class TokoChatReminderTickerUiModel(
    val message: String,
    val tickerType: Int,
    val showCloseButton: Boolean = false,
    val tag: String = ""
) {
    val impressHolder = ImpressHolder()
}
