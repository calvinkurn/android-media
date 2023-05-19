package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder

data class TokoChatReminderTickerUiModel(
    val message: String,
    val tickerType: Int,
    val showCloseButton: Boolean = false,
    val tag: String = ""
) {
    val impressHolder = ImpressHolder()
}
