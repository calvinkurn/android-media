package com.tokopedia.shop.score.performance.presentation.adapter.tablet

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.ShopPerformanceListener
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet.ItemDetailPerformanceTabletViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet.PeriodDetailPerformanceTabletViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet.ProtectedParameterTabletViewHolder
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemDetailPerformanceTabletUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.PeriodDetailTabletUiModel
import com.tokopedia.shop.score.performance.presentation.model.tablet.ProtectedParameterTabletUiModel

class DetailPerformanceAdapterTabletTypeFactory(
    private val shopPerformanceListener: ShopPerformanceListener,
) : BaseAdapterTypeFactory(), DetailPerformanceTabletTypeFactory {

    override fun type(periodDetailTabletUiModel: PeriodDetailTabletUiModel): Int {
        return PeriodDetailPerformanceTabletViewHolder.LAYOUT
    }

    override fun type(itemDetailPerformanceTabletUiModel: ItemDetailPerformanceTabletUiModel): Int {
        return ItemDetailPerformanceTabletViewHolder.LAYOUT
    }

    override fun type(protectedParameterTabletUiModel: ProtectedParameterTabletUiModel): Int {
        return ProtectedParameterTabletViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            PeriodDetailPerformanceTabletViewHolder.LAYOUT -> PeriodDetailPerformanceTabletViewHolder(parent)
            ItemDetailPerformanceTabletViewHolder.LAYOUT -> ItemDetailPerformanceTabletViewHolder(
                parent,
                shopPerformanceListener
            )
            ProtectedParameterTabletViewHolder.LAYOUT -> ProtectedParameterTabletViewHolder(
                parent,
                shopPerformanceListener
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}