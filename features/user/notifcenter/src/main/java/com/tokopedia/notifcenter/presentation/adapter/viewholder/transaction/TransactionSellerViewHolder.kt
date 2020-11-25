package com.tokopedia.notifcenter.presentation.adapter.viewholder.transaction

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.applink.ApplinkConst.SELLER_HISTORY
import com.tokopedia.notifcenter.data.consts.GlobalNavConstant.PENJUALAN
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.viewbean.SellerNotificationViewBean
import com.tokopedia.notifcenter.listener.TransactionMenuListener
import com.tokopedia.notifcenter.presentation.adapter.TransactionSectionAdapter
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseTransactionViewHolder

class TransactionSellerViewHolder(
        view: View,
        listener: TransactionMenuListener
) : BaseTransactionViewHolder<SellerNotificationViewBean>(view, listener) {

    override fun bind(element: SellerNotificationViewBean) {
        if (element.id != PENJUALAN) return

        super.bind(element)
        setHeader(element.title, SELLER_HISTORY)
        initChildItem(element)
    }

    override fun initChildItem(element: SellerNotificationViewBean) {
        lstMenuItem?.adapter = TransactionSectionAdapter(
                element.sellerItem(),
                listener
        )
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_transaction_seller
    }

}