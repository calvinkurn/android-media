package com.tokopedia.tokochat_common.view.listener

import com.tokopedia.tokochat_common.view.uimodel.TokoChatReminderTickerUiModel

interface TokochatReminderTickerListener {
    fun trackSeenTicker(element: TokoChatReminderTickerUiModel)
    fun onClickLinkReminderTicker(element: TokoChatReminderTickerUiModel, linkUrl: String)
    fun onCloseReminderTicker(element: TokoChatReminderTickerUiModel, position: Int)
}
