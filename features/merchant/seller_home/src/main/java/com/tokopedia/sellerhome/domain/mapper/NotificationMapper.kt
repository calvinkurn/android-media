package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhome.domain.model.ChatModel
import com.tokopedia.sellerhome.domain.model.GetNotificationsResponse
import com.tokopedia.sellerhome.domain.model.NotifCenterUnreadModel
import com.tokopedia.sellerhome.domain.model.SellerOrderStatusModel
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 2020-03-03
 */

class NotificationMapper @Inject constructor() {

    fun mapRemoteModelToUiModel(notification: GetNotificationsResponse): NotificationUiModel {
        return NotificationUiModel(
                chat = getChatUiModel(notification.notifications?.chat),
                notifCenterUnread = getNotifCenterUnreadUiModel(notification.notifCenterUnread),
                sellerOrderStatus = getSellerOrderStatusUiModel(notification.notifications?.sellerOrderStatus)
        )
    }

    private fun getChatUiModel(model: ChatModel?): Int {
        return model?.unreadsSeller.orZero()
    }

    private fun getNotifCenterUnreadUiModel(model: NotifCenterUnreadModel?): Int {
        return model?.notifUnreadInt.orZero()
    }

    private fun getSellerOrderStatusUiModel(model: SellerOrderStatusModel?): NotificationSellerOrderStatusUiModel {
        if (null == model) return NotificationSellerOrderStatusUiModel()
        return NotificationSellerOrderStatusUiModel(
                newOrder = model.newOrder,
                readyToShip = model.readyToShip
        )
    }
}