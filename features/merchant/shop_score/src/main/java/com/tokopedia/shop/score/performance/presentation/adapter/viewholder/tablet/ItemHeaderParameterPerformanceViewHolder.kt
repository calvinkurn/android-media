package com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemHeaderParameterDetailUiModel

class ItemHeaderParameterPerformanceViewHolder(
    view: View,
    private val shopPerformanceListener: ShopPerformanceListener
): AbstractViewHolder<ItemHeaderParameterDetailUiModel>(view) {

    companion object {
        val LAYOUT = 12345
    }

    override fun bind(element: ItemHeaderParameterDetailUiModel?) {

    }

}