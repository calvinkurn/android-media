package com.tokopedia.sellerhome.domain.mapper

import com.tokopedia.sellerhome.domain.model.ChatModel
import com.tokopedia.sellerhome.domain.model.GetNotificationsResponse
import com.tokopedia.sellerhome.domain.model.NotifCenterUnreadModel
import com.tokopedia.sellerhome.domain.model.SellerOrderStatusModel
import com.tokopedia.sellerhome.view.model.NotificationCenterUnreadUiModel
import com.tokopedia.sellerhome.view.model.NotificationChatUiModel
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

    private fun getChatUiModel(model: ChatModel?): NotificationChatUiModel {
        if (null == model) return NotificationChatUiModel()
        return NotificationChatUiModel(
                unreads = model.unreads,
                unreadsUser = model.unreadsUser,
                unreadsSeller = model.unreadsSeller
        )
    }

    private fun getNotifCenterUnreadUiModel(model: NotifCenterUnreadModel?): NotificationCenterUnreadUiModel {
        if (null == model) return NotificationCenterUnreadUiModel()
        return NotificationCenterUnreadUiModel(
                notifUnread = model.notifUnread,
                notifUnreadInt = model.notifUnreadInt
        )
    }

    private fun getSellerOrderStatusUiModel(model: SellerOrderStatusModel?): NotificationSellerOrderStatusUiModel {
        if (null == model) return NotificationSellerOrderStatusUiModel()
        return NotificationSellerOrderStatusUiModel(
                arriveAtDestination = model.arriveAtDestination,
                newOrder = model.newOrder,
                readyToShip = model.readyToShip,
                shipped = model.shipped
        )
    }
}