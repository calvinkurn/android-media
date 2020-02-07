package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.sellerhomedrawer.data.drawernotification.InfoPenjualNotification
import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.data.header.TopChatNotificationModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class NewNotificationUseCase @Inject constructor(val notificationUseCase: NotificationUseCase,
                                                 val deleteNotificationCacheUseCase: DeleteNotificationCacheUseCase,
                                                 val getChatNotificationUseCase: GetChatNotificationUseCase,
                                                 val getInfoPenjualNotificationUseCase: GetInfoPenjualNotificationUseCase,
                                                 val drawerCache: LocalCacheHandler): UseCase<NotificationModel>() {

    var isRefresh = false

    override fun createObservable(requestParams: RequestParams): Observable<NotificationModel> {
        if (isRefresh) {
            getChatNotificationUseCase.setRefresh(true)
            getInfoPenjualNotificationUseCase.setRefresh(true)
            return deleteNotificationCacheUseCase.createObservable()
                    .flatMap { getNotification(requestParams) }
        } else {
            getChatNotificationUseCase.setRefresh(false)
            getInfoPenjualNotificationUseCase.setRefresh(false)
            return getNotification(requestParams)
        }
    }

    private fun getNotification(requestParams: RequestParams): Observable<NotificationModel> {
        val notif = notificationUseCase.createObservable(requestParams)
        val notifTopChat = getChatNotificationUseCase.createObservable(RequestParams.EMPTY)
        val infoPenjualNotification = getInfoPenjualNotificationUseCase.createObservable(
                GetInfoPenjualNotificationUseCase.createParams(2)
        )

        return Observable.zip<NotificationModel, TopChatNotificationModel, InfoPenjualNotification, NotificationModel>(notif, notifTopChat, infoPenjualNotification, { notificationModel, chatNotificationModel, infoPenjualNotif ->
            val data = notificationModel.getNotificationData()
            data.setTotalNotif(data.getTotalNotif() - data.getInbox().getInboxMessage() +
                    chatNotificationModel.getNotifUnreadsSeller() + infoPenjualNotif.getNotifUnreadInt()!!.toInt())
            data.getInbox().setInboxMessage(chatNotificationModel.getNotifUnreadsSeller())
            notificationModel.setNotificationData(data)
            val notifUnreadsSeller = chatNotificationModel.getNotifUnreadsSeller()
            val notifInfoPenjual = infoPenjualNotif.getNotifUnreadInt()!!.toInt()
            drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_MESSAGE, notifUnreadsSeller)
            drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_SELLER_INFO, notifInfoPenjual)
            drawerCache.putInt(SellerDrawerNotification.CACHE_TOTAL_NOTIF, data.getTotalNotif())
            drawerCache.applyEditor()
            notificationModel
        })
    }


}