package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder.*
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentTickerUiModel

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderAdapterTypeFactory(
        private val itemClickListener: BaseListAdapter.OnAdapterInteractionListener<Visitable<WaitingPaymentOrderAdapterTypeFactory>>
) : BaseAdapterTypeFactory() {

    fun type(waitingPaymentTickerUiModel: WaitingPaymentTickerUiModel): Int {
        return WaitingPaymentTickerViewHolder.LAYOUT
    }

    fun type(waitingPaymentOrderUiModel: WaitingPaymentOrderUiModel): Int {
        return WaitingPaymentOrdersViewHolder.LAYOUT
    }

    override fun type(viewModel: ErrorNetworkModel?): Int {
        return WaitingPaymentOrdersErrorViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return WaitingPaymentOrderLoadingViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return WaitingPaymentOrderEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            WaitingPaymentOrdersViewHolder.LAYOUT -> WaitingPaymentOrdersViewHolder(parent)
            WaitingPaymentTickerViewHolder.LAYOUT -> WaitingPaymentTickerViewHolder(parent, itemClickListener)
            WaitingPaymentOrdersErrorViewHolder.LAYOUT -> WaitingPaymentOrdersErrorViewHolder(parent)
            WaitingPaymentOrderLoadingViewHolder.LAYOUT -> WaitingPaymentOrderLoadingViewHolder(parent)
            WaitingPaymentOrderEmptyViewHolder.LAYOUT -> WaitingPaymentOrderEmptyViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}