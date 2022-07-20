package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemTickerReactivatedSellerBinding
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.TickerReactivatedUiModel
import com.tokopedia.unifycomponents.ticker.TickerCallback

class TickerReactivatedViewHolder(
    view: View,
    private val shopPerformanceListener: ShopPerformanceListener
) :
    AbstractViewHolder<TickerReactivatedUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_ticker_reactivated_seller
    }

    private val binding = ItemTickerReactivatedSellerBinding.bind(itemView)

    override fun bind(element: TickerReactivatedUiModel) {
        with(binding) {
            tickerReactivatedSeller.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                override fun onDismiss() {
                    shopPerformanceListener.onCloseTickerClicked()
                }
            })
        }
    }
}