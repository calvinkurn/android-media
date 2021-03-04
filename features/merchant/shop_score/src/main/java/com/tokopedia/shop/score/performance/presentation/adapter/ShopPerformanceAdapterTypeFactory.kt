package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemDetailPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemHeaderShopPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.PeriodDetailPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel

class ShopPerformanceAdapterTypeFactory(private val shopPerformanceListener: ShopPerformanceListener,
                                        private val itemShopPerformanceListener: ItemShopPerformanceListener)
    : BaseAdapterTypeFactory(), ShopPerformanceTypeFactory {

    override fun type(headerShopPerformanceUiModel: HeaderShopPerformanceUiModel): Int {
        return ItemHeaderShopPerformanceViewHolder.LAYOUT
    }

    override fun type(periodDetailPerformanceUiModel: PeriodDetailPerformanceUiModel): Int {
        return PeriodDetailPerformanceViewHolder.LAYOUT
    }

    override fun type(itemDetailPerformanceUiModel: ItemDetailPerformanceUiModel): Int {
        return ItemDetailPerformanceViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemHeaderShopPerformanceViewHolder.LAYOUT -> ItemHeaderShopPerformanceViewHolder(parent, shopPerformanceListener)
            PeriodDetailPerformanceViewHolder.LAYOUT -> PeriodDetailPerformanceViewHolder(parent)
            ItemDetailPerformanceViewHolder.LAYOUT -> ItemDetailPerformanceViewHolder(parent, itemShopPerformanceListener)
            else -> return super.createViewHolder(parent, type)
        }
    }
}