package com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.notifcenter.GlobalNavConstant.*
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.model.DrawerNotification
import com.tokopedia.notifcenter.data.viewbean.SaleNotificationViewBean
import com.tokopedia.notifcenter.listener.TransactionMenuListener
import com.tokopedia.notifcenter.presentation.adapter.TransactionSectionAdapter
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseTransactionViewHolder
import com.tokopedia.notifcenter.data.model.DrawerNotification.ChildDrawerNotification as ChildDrawerNotification

class TransactionSellerViewHolder(
        view: View,
        val listener: TransactionMenuListener
): BaseTransactionViewHolder<SaleNotificationViewBean>(view) {

    override fun bind(element: SaleNotificationViewBean) {
        super.bind(element)
        sellerNotificationMore(element)
        isHasWaitingPayment(false)
        initChildItem(element)
    }

    override fun onLoadMoreClicked(element: SaleNotificationViewBean) {
        listener.sendTrackingData(element.title, btnLoadMore.text.toString())
        RouteManager.route(context, ApplinkConst.SELLER_HISTORY)
    }

    private fun sellerNotificationMore(element: SaleNotificationViewBean) {
        setHeader(element.title) {
            onLoadMoreClicked(element)
        }
    }

    private fun initChildItem(notification: DrawerNotification) {
        val childNotification = mutableListOf<ChildDrawerNotification>()
        notification.childs.forEach {
            when (it.id) {
                PESANAN_BARU -> childNotification.add(it)
                SIAP_DIKIRIM -> childNotification.add(it)
                SEDANG_DIKIRIM -> childNotification.add(it)
                SAMPAI_TUJUAN -> childNotification.add(it)
            }
        }

        val newData = DrawerNotification().apply {
            childs = childNotification
            if (notification.id == PENJUALAN) {
                id = notification.id
                title = notification.title
            }
        }

        lstMenuItem.adapter = TransactionSectionAdapter(newData, listener)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_transaction
    }

}