package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class TickerViewHolder(itemView: View?) : AbstractViewHolder<TickerUiModel>(itemView), TickerCallback {

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

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        RouteManager.route(itemView.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
    }

    override fun onDismiss() {}

    private fun setupTicker(element: TickerUiModel) {
        (itemView as? Ticker)?.apply {
            val tickerDescription = composeActionText(element.description, element.actionText, element.actionUrl)
            setHtmlDescription(tickerDescription)
            setDescriptionClickEvent(this@TickerViewHolder)
        }
    }

    private fun composeActionText(description: String, actionText: String, actionUrl: String): String {
        return itemView.context.getString(R.string.html_link, description, actionUrl, actionText)
    }
}