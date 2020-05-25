package com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.ApplinkConst.PURCHASE_ORDER
import com.tokopedia.applink.RouteManager
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.MENUNGGU_PEMBAYARAN
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PEMBELIAN
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.viewbean.BuyerNotificationViewBean
import com.tokopedia.notifcenter.listener.TransactionMenuListener
import com.tokopedia.notifcenter.presentation.adapter.TransactionSectionAdapter
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseTransactionViewHolder

class TransactionBuyerViewHolder(
        view: View,
        listener: TransactionMenuListener
) : BaseTransactionViewHolder<BuyerNotificationViewBean>(view, listener) {

    override fun bind(element: BuyerNotificationViewBean) {
        if (element.id != PEMBELIAN) return

        super.bind(element)
        setHeader(element.title, PURCHASE_ORDER)
        waitingPayment(element)
        initChildItem(element)
    }

    override fun initChildItem(element: BuyerNotificationViewBean) {
        lstMenuItem?.adapter = TransactionSectionAdapter(
                element.buyerItem(),
                listener
        )
    }

    private fun waitingPayment(element: BuyerNotificationViewBean) {
        val notification = element.childs.first()
        if (notification.id == MENUNGGU_PEMBAYARAN) {
            addWaitingPaymentMenu(notification.badge) {
                RouteManager.route(context, notification.applink)
                listener.sendTrackingData(element.title, notification.title)
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_transaction_buyer
    }

}