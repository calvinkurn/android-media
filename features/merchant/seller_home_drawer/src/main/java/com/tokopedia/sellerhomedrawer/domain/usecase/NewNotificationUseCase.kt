package com.tokopedia.sellerhomedrawer.domain.usecase

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject

class NewNotificationUseCase @Inject constructor(val notificationUseCase: NotificationUseCase,
                                                 val deleteNotificationCacheUseCase: DeleteNotificationCacheUseCase,
                                                 val getChatNotificationUseCase: GetChatNotificationUseCase,
                                                 val getInfoPenjualNotificationUseCase: GetInfoPenjualNotificationUseCase,
                                                 val drawerCache: LocalCacheHandler): UseCase<NotificationModel>() {

    companion object {
        const val SELLER_TYPE_ID = 2
    }

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
        val notif = notificationUseCase.createObservable(requestParams).subscribeOn(Schedulers.io())
        val notifTopChat = getChatNotificationUseCase.createObservable(RequestParams.EMPTY).subscribeOn(Schedulers.io())
        val infoPenjualNotification = getInfoPenjualNotificationUseCase.createObservable(
                GetInfoPenjualNotificationUseCase.createParams(SELLER_TYPE_ID)
        ).subscribeOn(Schedulers.io())

        return Observable.zip(notif, notifTopChat, infoPenjualNotification) {
            notificationModel, chatNotificationModel, infoPenjualNotif ->
                val data = notificationModel.notificationData
                data.totalNotif = (data.totalNotif - data.inbox.inboxMessage +
                        chatNotificationModel.notifUnreadsSeller + infoPenjualNotif.notifUnreadInt.toInt())
                data.inbox.inboxMessage = chatNotificationModel.notifUnreadsSeller
                notificationModel.notificationData = data
                val notifUnreadsSeller = chatNotificationModel.notifUnreadsSeller
                val notifInfoPenjual = infoPenjualNotif.notifUnreadInt.toInt()
                drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_MESSAGE, notifUnreadsSeller)
                drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_SELLER_INFO, notifInfoPenjual)
                drawerCache.putInt(SellerDrawerNotification.CACHE_TOTAL_NOTIF, data.totalNotif)
                drawerCache.applyEditor()
                notificationModel
        }
    }


}