package com.tokopedia.shop.score.performance.presentation.adapter.tablet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemDetailPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemProtectedParameterSectionViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.PeriodDetailPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ProtectedParameterSectionUiModel

class DetailPerformanceAdapterTabletTabletTypeFactory(
    private val shopPerformanceListener: ShopPerformanceListener,
) : BaseAdapterTypeFactory(), DetailPerformanceTabletTypeFactory {

    override fun type(periodDetailPerformanceUiModel: PeriodDetailPerformanceUiModel): Int {
        return PeriodDetailPerformanceViewHolder.LAYOUT
    }

    override fun type(itemDetailPerformanceUiModel: ItemDetailPerformanceUiModel): Int {
        return ItemDetailPerformanceViewHolder.LAYOUT
    }

    override fun type(protectedParameterSectionUiModel: ProtectedParameterSectionUiModel): Int {
        return ItemProtectedParameterSectionViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PeriodDetailPerformanceViewHolder.LAYOUT -> PeriodDetailPerformanceViewHolder(parent)
            ItemDetailPerformanceViewHolder.LAYOUT -> ItemDetailPerformanceViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemProtectedParameterSectionViewHolder.LAYOUT -> ItemProtectedParameterSectionViewHolder(
                parent,
                shopPerformanceListener
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}