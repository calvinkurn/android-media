package com.tokopedia.tokochat.common.view.chatroom.listener

import com.tokopedia.tokochat.common.view.chatroom.uimodel.TokoChatReminderTickerUiModel

interface TokochatReminderTickerListener {
    fun trackSeenTicker(element: TokoChatReminderTickerUiModel)
    fun onClickLinkReminderTicker(element: TokoChatReminderTickerUiModel, linkUrl: String)
    fun onCloseReminderTicker(element: TokoChatReminderTickerUiModel, position: Int)
}
