package com.tokopedia.notifcenter.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.navigation_common.model.NotificationsModel
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.MENUNGGU_KONFIRMASI
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.MENUNGGU_PEMBAYARAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PEMBELIAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PENJUALAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PESANAN_BARU
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PESANAN_DIPROSES
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SAMPAI_TUJUAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SEDANG_DIKIRIM
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.SIAP_DIKIRIM
import com.tokopedia.notifcenter.data.model.DrawerNotification
import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.data.viewbean.BuyerNotificationViewBean
import com.tokopedia.notifcenter.data.viewbean.SellerNotificationViewBean
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactoryImpl

class NotificationTransactionAdapter(
        notificationFactory: NotificationTransactionFactoryImpl
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(notificationFactory) {

    fun updateValue(notificationData: NotificationsModel) {
        data.forEach {
            when (it) {
                is BuyerNotificationViewBean -> {
                    it.childs.forEach { child ->
                        if (it.id == PEMBELIAN) {
                            purchaseCounterBadge(notificationData, child)
                        }
                    }
                }
                is SellerNotificationViewBean -> {
                    it.childs.forEach { child ->
                        if (it.id == PENJUALAN) {
                            saleCounterBadge(notificationData, child)
                        }
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun markAllAsRead() {
        for (data in visitables) {
            if(data is NotificationItemViewBean) {
                data.isRead = true
            }
            notifyItemChanged(visitables.indexOf(data))
        }
    }

    private fun purchaseCounterBadge(
            data: NotificationsModel,
            child: DrawerNotification.ChildDrawerNotification) {
        when (child.id) {
            MENUNGGU_PEMBAYARAN -> child.badge = data.buyerOrder.paymentStatus.toInt()
            MENUNGGU_KONFIRMASI -> child.badge = data.buyerOrder.confirmed
            PESANAN_DIPROSES -> child.badge = data.buyerOrder.processed
            SEDANG_DIKIRIM -> child.badge = data.buyerOrder.shipped
            SAMPAI_TUJUAN -> child.badge = data.buyerOrder.arriveAtDestination
        }
    }

    private fun saleCounterBadge(
            data: NotificationsModel,
            child: DrawerNotification.ChildDrawerNotification) {
        when (child.id) {
            PESANAN_BARU -> child.badge = data.sellerOrder.newOrder
            SIAP_DIKIRIM -> child.badge = data.sellerOrder.readyToShip
            SEDANG_DIKIRIM -> child.badge = data.sellerOrder.shipped
            SAMPAI_TUJUAN -> child.badge = data.sellerOrder.arriveAtDestination
        }
    }

    fun removeItem() {
        visitables.removeAll { it is NotificationItemViewBean }
        notifyDataSetChanged()
    }

    fun removeEmptyState() {
        visitables.removeAll { it is NotificationEmptyStateViewBean }
        notifyDataSetChanged()
    }

}