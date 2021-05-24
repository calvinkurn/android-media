package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.Utils
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class TickerViewHolder(
        itemView: View?,
        private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<TickerUiModel>(itemView), TickerCallback {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_ticker
    }

    private var element: TickerUiModel? = null

    override fun bind(element: TickerUiModel?) {
        element?.let {
            this.element = it
            setupTicker(element)
        }
    }

    override fun bind(element: TickerUiModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        navigator.openWebView(linkUrl.toString())
    }

    override fun onDismiss() {}

    private fun setupTicker(element: TickerUiModel) {
        (itemView as? Ticker)?.apply {
            val tickerDescription = composeActionText(element.description, element.actionText, element.actionUrl)
            setHtmlDescription(tickerDescription)
            setDescriptionClickEvent(this@TickerViewHolder)
            tickerType = Utils.mapTickerType(element.type)
        }
    }

    private fun composeActionText(description: String, actionText: String, actionUrl: String): String {
        return itemView.context.getString(R.string.html_link, description, actionUrl, actionText)
    }
}