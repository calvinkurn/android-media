package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.ItemTimerNewSellerUiModel

class ItemTimerNewSellerViewHolder(view: View): AbstractViewHolder<ItemTimerNewSellerUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.timer_new_seller_before_transition
    }

    override fun bind(element: ItemTimerNewSellerUiModel?) {
        with(itemView) {

        }
    }
}