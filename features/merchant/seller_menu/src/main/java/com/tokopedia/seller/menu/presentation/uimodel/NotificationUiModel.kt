package com.tokopedia.seller.menu.presentation.uimodel

data class NotificationUiModel(
    val inboxTalkUnread: Int,
    val notifCenterTotalUnread: Int,
    val order: ShopOrderUiModel,
    val resolutionCount: Int
)