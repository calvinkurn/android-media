package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyTickerUiModel

class ItemPenaltyTickerViewHolder(view: View): AbstractViewHolder<ItemPenaltyTickerUiModel>(view) {

    override fun bind(element: ItemPenaltyTickerUiModel?) {}

    companion object {
        val LAYOUT = R.layout.item_penalty_ticker
    }

}
