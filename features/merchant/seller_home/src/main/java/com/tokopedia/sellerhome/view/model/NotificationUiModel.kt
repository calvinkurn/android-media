package com.tokopedia.sellerhome.view.model

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

data class NotificationUiModel(
        val chat: Int,
        val notifCenterUnread: Int,
        val sellerOrderStatus: NotificationSellerOrderStatusUiModel
)