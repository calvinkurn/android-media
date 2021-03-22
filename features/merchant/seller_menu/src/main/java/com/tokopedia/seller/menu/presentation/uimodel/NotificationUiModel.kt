package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.seller.menu.common.view.uimodel.ShopOrderUiModel

data class NotificationUiModel(
    val inboxTalkUnread: Int,
    val notifCenterTotalUnread: Int,
    val order: ShopOrderUiModel,
    val resolutionCount: Int
)