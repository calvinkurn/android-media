package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.TickerReactivatedUiModel

class TickerReactivatedViewHolder(view: View) : AbstractViewHolder<TickerReactivatedUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_ticker_reactivated_seller
    }

    override fun bind(element: TickerReactivatedUiModel) {}
}