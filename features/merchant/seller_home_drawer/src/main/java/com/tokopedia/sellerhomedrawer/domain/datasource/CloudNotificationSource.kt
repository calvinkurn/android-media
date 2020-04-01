package com.tokopedia.sellerhomedrawer.domain.datasource

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.TKPDMapParam
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.domain.mapper.NotificationMapper
import com.tokopedia.sellerhomedrawer.domain.service.NotificationService
import com.tokopedia.user.session.UserSession
import rx.Observable
import rx.functions.Action1
import javax.inject.Inject

/**
 * Created by nisie on 5/5/17.
 */

class CloudNotificationSource @Inject constructor(@ApplicationContext val context: Context,
                                                  val notificationService: NotificationService,
                                                  val notificationMapper: NotificationMapper,
                                                  val drawerCache: LocalCacheHandler,
                                                  val userSession: UserSession) {

    fun getNotification(params: TKPDMapParam<String, Any>): Observable<NotificationModel> {
        return notificationService.notificationApi
                .getNotification2(AuthUtil.generateParamsNetwork2(context, params, userSession.deviceId, userSession.userId))
                .map(notificationMapper)
                .doOnNext(saveToCache())
    }

    private fun saveToCache(): Action1<NotificationModel> {
        return Action1 { notificationModel ->
            if (notificationModel != null && notificationModel.isSuccess) {
                val notificationData = notificationModel.notificationData
                drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_TALK, notificationData.inbox.inboxTalk)
                drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_REVIEW, notificationData.inbox.inboxReputation)
                drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_TICKET, notificationData.inbox.inboxTicket)

                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_DELIVERY_CONFIRM, notificationData.purchase.purchaseDeliveryConfirm)
                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_ORDER_STATUS, notificationData.purchase.purchaseOrderStatus)
                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_PAYMENT_CONFIRM, notificationData.purchase.purchasePaymentConfirm)
                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_REORDER, notificationData.purchase.purchaseReorder)
                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_PAYMENT_CONF, notificationData.purchase.purchasePaymentConf)

                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_CONFIRMED, notificationData.buyerOrder.confirmed)
                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_PROCESSED, notificationData.buyerOrder.processed)
                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_SHIPPED, notificationData.buyerOrder.shipped)
                drawerCache.putInt(SellerDrawerNotification.CACHE_PURCHASE_DELIVERED, notificationData.buyerOrder.arriveAtDestination)

                drawerCache.putInt(SellerDrawerNotification.CACHE_SELLING_NEW_ORDER, notificationData.sales.salesNewOrder)
                drawerCache.putInt(SellerDrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, notificationData.sales.salesShippingConfirm)
                drawerCache.putInt(SellerDrawerNotification.CACHE_SELLING_SHIPPING_STATUS, notificationData.sales.salesShippingStatus)

                drawerCache.putInt(SellerDrawerNotification.CACHE_TOTAL_NOTIF, notificationData
                        .totalNotif - notificationData.inbox.inboxMessage)
                drawerCache.putInt(SellerDrawerNotification.CACHE_INCR_NOTIF, notificationData.incrNotif)

                drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_BUYER, notificationData.resolutionModel.resolutionBuyer)
                drawerCache.putInt(SellerDrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_SELLER, notificationData.resolutionModel.resolutionSeller)

                drawerCache.applyEditor()
            }
        }
    }
}
