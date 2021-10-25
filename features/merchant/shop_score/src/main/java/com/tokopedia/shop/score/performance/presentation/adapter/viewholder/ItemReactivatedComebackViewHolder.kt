package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.ItemReactivatedComebackUiModel

class ItemReactivatedComebackViewHolder(
    view: View,
    shopPerformanceListener: ShopPerformanceListener
) : AbstractViewHolder<ItemReactivatedComebackUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_reactivated_seller_comeback
    }

    override fun bind(element: ItemReactivatedComebackUiModel) {

    }

}