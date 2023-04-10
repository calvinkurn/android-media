package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class OwocTickerViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator,
) : AbstractViewHolder<TickerUiModel>(itemView), TickerCallback {

    companion object {
        val LAYOUT = R.layout.item_owoc_ticker
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
        navigator.openAppLink(linkUrl.toString(), false)
    }

    override fun onDismiss() {}

    private fun setupTicker(element: TickerUiModel) {
        (itemView as? Ticker)?.apply {
            val tickerDescription = composeDescriptionText(element.description, element.actionText, element.actionUrl)
            setHtmlDescription(tickerDescription)
            setDescriptionClickEvent(this@OwocTickerViewHolder)
            tickerType = Utils.mapTickerType(element.type)
        }
    }

    private fun composeDescriptionText(description: String, actionText: String, actionUrl: String): String {
        return if (actionText.isNotBlank() && actionUrl.isNotBlank()) {
            itemView.context.getString(R.string.html_link, description, actionUrl, actionText).trim()
        } else {
            StringBuilder().apply {
                if (description.isNotBlank()) {
                    append(description)
                }
                if (actionText.isNotBlank()) {
                    if (isNotBlank()) append(" ")
                    append(actionText)
                }
            }.toString()
        }
    }
}
