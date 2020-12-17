package com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class WaitingPaymentTickerViewHolder(
        itemView: View?,
        private val itemClickListener: BaseListAdapter.OnAdapterInteractionListener<Visitable<WaitingPaymentOrderAdapterTypeFactory>>
) : AbstractViewHolder<WaitingPaymentTickerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_waiting_payment_orders_ticker
    }

    override fun bind(element: WaitingPaymentTickerUiModel?) {
        element?.let { element ->
            with(itemView as Ticker) {
                tickerType = element.type
                tickerTitle = element.title
                setHtmlDescription(element.description)
                closeButtonVisibility = if (element.showCloseIcon) View.VISIBLE else View.GONE
                itemView.setDescriptionClickEvent(object: TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        itemClickListener.onItemClicked(element)
                    }

                    override fun onDismiss() {}
                })
            }
        }
    }
}