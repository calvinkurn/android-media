package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder

data class TokochatReminderTickerUiModel(
    val message: String,
    val tickerType: Int
) {
    val impressHolder = ImpressHolder()
}
