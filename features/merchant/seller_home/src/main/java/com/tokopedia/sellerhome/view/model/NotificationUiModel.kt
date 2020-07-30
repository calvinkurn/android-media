package com.tokopedia.sellerhome.view.model

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

data class NotificationUiModel(
        val chat: NotificationChatUiModel,
        val notifCenterUnread: NotificationCenterUnreadUiModel,
        val sellerOrderStatus: NotificationSellerOrderStatusUiModel
)