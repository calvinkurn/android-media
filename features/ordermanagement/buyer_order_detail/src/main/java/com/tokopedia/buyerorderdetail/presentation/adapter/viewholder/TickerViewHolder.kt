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

class TickerViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator,
    private val listener: TickerViewHolderListener
) : AbstractViewHolder<TickerUiModel>(itemView), TickerCallback {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_ticker

        private val refreshableTickerKey = listOf(
            BuyerOrderDetailMiscConstant.TICKER_KEY_SELLER_ORDER_EXTENSION
        )
    }

    private var element: TickerUiModel? = null

    override fun bind(element: TickerUiModel?) {
        element?.let {
            this.element = it
            setupTicker(element)
            val marginVertical = if (element.marginTop == null && element.marginBottom == null) {
                null
            } else {
                Pair(element.marginTop, element.marginBottom)
            }
            setupMarginTicker(marginVertical)
        }
    }

    override fun bind(element: TickerUiModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        navigator.openAppLink(linkUrl.toString(), shouldRefreshWhenBack())
        if (element?.actionKey == BuyerOrderDetailMiscConstant.TICKER_KEY_SHIPPING_INFO) {
            listener.onClickShipmentInfoTnC()
        }
    }

    override fun onDismiss() {}

    private fun setupTicker(element: TickerUiModel) {
        (itemView as? Ticker)?.apply {
            val tickerDescription = composeDescriptionText(element.description, element.actionText, element.actionUrl)
            setHtmlDescription(tickerDescription)
            setDescriptionClickEvent(this@TickerViewHolder)
            tickerType = Utils.mapTickerType(element.type)
        }
    }

    private fun setupMarginTicker(marginVertical: Pair<Int?, Int?>?) {
        marginVertical?.let {
            (itemView as? Ticker)?.apply {
                val layoutParamsMargin = layoutParams as? RecyclerView.LayoutParams
                marginVertical.first?.let { marginBottom ->
                    layoutParamsMargin?.bottomMargin = marginBottom
                }
                marginVertical.second?.let { marginTop ->
                    layoutParamsMargin?.topMargin = marginTop
                }
                layoutParams = layoutParamsMargin
            }
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

    private fun shouldRefreshWhenBack(): Boolean {
        return refreshableTickerKey.contains(element?.actionKey.orEmpty())
    }

    interface TickerViewHolderListener {
        fun onClickShipmentInfoTnC()
    }
}
