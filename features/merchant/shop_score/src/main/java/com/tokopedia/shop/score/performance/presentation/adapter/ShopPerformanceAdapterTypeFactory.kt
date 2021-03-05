package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.performance.presentation.model.*

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

    override fun type(transitionPeriodReliefUiModel: TransitionPeriodReliefUiModel): Int {
        return TransitionPeriodReliefViewHolder.LAYOUT
    }

    override fun type(itemCurrentStatusPMUiModel: ItemCurrentStatusPMUiModel): Int {
        return ItemCurrentStatusPMViewHolder.LAYOUT
    }

    override fun type(itemPotentialPMBenefitUIModel: SectionPotentialPMBenefitUiModel): Int {
        return CardPotentialPMBenefitViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ShopPerformanceShimmerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemHeaderShopPerformanceViewHolder.LAYOUT -> ItemHeaderShopPerformanceViewHolder(parent, shopPerformanceListener)
            PeriodDetailPerformanceViewHolder.LAYOUT -> PeriodDetailPerformanceViewHolder(parent)
            ItemDetailPerformanceViewHolder.LAYOUT -> ItemDetailPerformanceViewHolder(parent, itemShopPerformanceListener)
            ShopPerformanceShimmerViewHolder.LAYOUT -> ShopPerformanceShimmerViewHolder(parent)
            TransitionPeriodReliefViewHolder.LAYOUT -> TransitionPeriodReliefViewHolder(parent)
            ItemCurrentStatusPMViewHolder.LAYOUT -> ItemCurrentStatusPMViewHolder(parent)
            CardPotentialPMBenefitViewHolder.LAYOUT -> CardPotentialPMBenefitViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}