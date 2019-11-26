package com.tokopedia.navigation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.navigation.GlobalNavConstant.*
import com.tokopedia.navigation.domain.model.*
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactoryImpl
import com.tokopedia.navigation_common.model.NotificationsModel

class NotificationTransactionAdapter(
        notificationFactory: NotificationTransactionFactoryImpl
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(notificationFactory) {

    fun updateValue(notificationData: NotificationsModel) {
        data.forEach {
            when (it) {
                is PurchaseNotification -> {
                    it.childs.forEach { child ->
                        if (it.id == PEMBELIAN) {
                            purchaseCounterBadge(notificationData, child)
                        }
                    }
                }
                is SaleNotification -> {
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

    private fun purchaseCounterBadge(
            data: NotificationsModel,
            child: DrawerNotification.ChildDrawerNotification) {
        when (child.id) {
            MENUNGGU_PEMBAYARAN -> child.badge = data.buyerOrder.paymentStatus.toInt()
            MENUNGGU_KONFIRMASI -> child.badge = data.buyerOrder.confirmed
            PESANAN_DIPROSES    -> child.badge = data.buyerOrder.processed
            SEDANG_DIKIRIM      -> child.badge = data.buyerOrder.shipped
            SAMPAI_TUJUAN       -> child.badge = data.buyerOrder.arriveAtDestination
        }
    }

    private fun saleCounterBadge(
            data: NotificationsModel,
            child: DrawerNotification.ChildDrawerNotification) {
        when (child.id) {
            PESANAN_BARU    -> child.badge = data.sellerOrder.newOrder
            SIAP_DIKIRIM    -> child.badge = data.sellerOrder.readyToShip
            SEDANG_DIKIRIM  -> child.badge = data.sellerOrder.shipped
            SAMPAI_TUJUAN   -> child.badge = data.sellerOrder.arriveAtDestination
        }
    }

    fun hideFilterItem() {
        for (section in visitables) {
            if (section is NotificationFilterSectionWrapper) {
                removeElement(section)
            }
        }
    }

    fun removeTransaction() {
        for (section in visitables) {
            if (section is TransactionItemNotification) {
                visitables.remove(section)
            }
        }
        notifyDataSetChanged()
    }

}