package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R

class WaitingPaymentOrderLoadingViewHolder(itemView: View?) : AbstractViewHolder<LoadingModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders_loading
    }

    override fun bind(element: LoadingModel?) {
        // noop
    }
}