package com.tokopedia.tokochat_common.view.listener

import com.tokopedia.tokochat_common.view.uimodel.TokochatReminderTickerUiModel

interface TokochatReminderTickerListener {
    fun trackSeenTicker(element: TokochatReminderTickerUiModel)
    fun onClickLinkReminderTicker(element: TokochatReminderTickerUiModel, linkUrl: String)
    fun onCloseReminderTicker(element: TokochatReminderTickerUiModel, position: Int)
}
