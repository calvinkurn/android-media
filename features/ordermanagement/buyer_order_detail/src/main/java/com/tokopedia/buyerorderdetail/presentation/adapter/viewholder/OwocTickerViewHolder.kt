package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.databinding.ItemOwocTickerBinding
import com.tokopedia.buyerorderdetail.presentation.model.OwocTickerUiModel
import com.tokopedia.buyerorderdetail.presentation.model.TickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class OwocTickerViewHolder(
    view: View?,
    private val navigator: BuyerOrderDetailNavigator?,
) : AbstractViewHolder<OwocTickerUiModel>(view), TickerCallback {

    companion object {
        val LAYOUT = R.layout.item_owoc_ticker
    }

    private val binding = ItemOwocTickerBinding.bind(itemView)

    private var element: OwocTickerUiModel? = null

    override fun bind(element: OwocTickerUiModel?) {
        element?.let {
            this.element = it
            setupTicker(element)
        }
    }

    override fun bind(element: OwocTickerUiModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onDescriptionViewClick(linkUrl: CharSequence) {
        navigator?.openAppLink(linkUrl.toString(), false)
    }

    override fun onDismiss() {}

    private fun setupTicker(element: OwocTickerUiModel) {
        binding.owocTicker.run {
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
