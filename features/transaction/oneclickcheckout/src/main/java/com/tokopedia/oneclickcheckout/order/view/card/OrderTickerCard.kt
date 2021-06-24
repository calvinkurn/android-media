package com.tokopedia.oneclickcheckout.order.view.card

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.databinding.CardOrderTickerBinding
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import com.tokopedia.unifycomponents.ticker.TickerCallback

class OrderTickerCard(private val binding: CardOrderTickerBinding, private val listener: OrderTickerCardListener): RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val VIEW_TYPE = 0
    }

    fun bind(ticker: TickerData?) {
        if (ticker != null) {
            binding.tickerOsp.tickerTitle = ticker.title
            binding.tickerOsp.setHtmlDescription(ticker.message)
            binding.tickerOsp.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    /* no-op */
                }

                override fun onDismiss() {
                    listener.onCloseTicker()
                }
            })
            binding.tickerOsp.visible()
        } else {
            binding.tickerOsp.gone()
        }
    }

    interface OrderTickerCardListener {
        fun onCloseTicker()
    }
}