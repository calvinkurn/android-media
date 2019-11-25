package com.tokopedia.navigation.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.navigation.GlobalNavConstant.*
import com.tokopedia.navigation.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.navigation.domain.model.PurchaseNotification
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactoryImpl
import com.tokopedia.navigation_common.model.NotificationsModel

class NotificationTransactionAdapter(
        notificationFactory: NotificationTransactionFactoryImpl
): BaseListAdapter<Visitable<*>, BaseAdapterTypeFactory>(notificationFactory) {

    fun updateValue(notifData: NotificationsModel) {
        for (item in data) {
            if (item is PurchaseNotification) {
                val childs = item.childs
                for (child in childs) {
                    if (item.id == PEMBELIAN) {
                        when (child.id) {
                            MENUNGGU_PEMBAYARAN -> {
                                try {
                                    child.badge = notifData.buyerOrder.paymentStatus.toInt()
                                } catch (e: NumberFormatException) {
                                    child.badge = 0
                                }
                            }
                            MENUNGGU_KONFIRMASI -> child.badge = notifData.buyerOrder.confirmed
                            PESANAN_DIPROSES -> child.badge = notifData.buyerOrder.processed
                            SEDANG_DIKIRIM -> child.badge = notifData.buyerOrder.shipped
                            SAMPAI_TUJUAN -> child.badge = notifData.buyerOrder.arriveAtDestination
                        }
                    } else if (item.id == PENJUALAN) {
                        when (child.id) {
                            PESANAN_BARU -> child.badge = notifData.sellerOrder.newOrder
                            SIAP_DIKIRIM -> child.badge = notifData.sellerOrder.readyToShip
                            SEDANG_DIKIRIM -> child.badge = notifData.sellerOrder.shipped
                            SAMPAI_TUJUAN -> child.badge = notifData.sellerOrder.arriveAtDestination
                        }
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun hideFilterItem() {
        data.forEach {
            if (it is NotificationFilterSectionWrapper) {
                it.visibility = false
            }
        }
        notifyDataSetChanged()
    }

}