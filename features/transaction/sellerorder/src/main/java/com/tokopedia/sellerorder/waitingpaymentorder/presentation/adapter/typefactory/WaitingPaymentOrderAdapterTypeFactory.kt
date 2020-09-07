package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder.WaitingPaymentOrderLoadingViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder.WaitingPaymentOrdersErrorViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder.WaitingPaymentOrdersViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder

class WaitingPaymentOrderAdapterTypeFactory : BaseAdapterTypeFactory() {
    fun type(waitingPaymentOrder: WaitingPaymentOrder): Int {
        return WaitingPaymentOrdersViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel?): Int {
        return WaitingPaymentOrdersErrorViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return WaitingPaymentOrderLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            WaitingPaymentOrdersViewHolder.LAYOUT -> WaitingPaymentOrdersViewHolder(parent)
            WaitingPaymentOrdersErrorViewHolder.LAYOUT -> WaitingPaymentOrdersErrorViewHolder(parent)
            WaitingPaymentOrderLoadingViewHolder.LAYOUT -> WaitingPaymentOrderLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}