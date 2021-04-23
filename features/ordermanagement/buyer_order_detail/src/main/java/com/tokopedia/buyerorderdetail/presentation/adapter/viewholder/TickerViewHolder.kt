package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker

class TickerViewHolder(itemView: View?) : AbstractViewHolder<TickerUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_ticker
    }

    override fun bind(element: TickerUiModel?) {
        element?.let {
            setupTicker(element)
        }
    }

    private fun setupTicker(element: TickerUiModel) {
        (itemView as? Ticker)?.apply {
            setHtmlDescription(element.description)
        }
    }
}