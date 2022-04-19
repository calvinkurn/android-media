package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet.ItemHeaderParameterPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.model.*
import com.tokopedia.shop.score.performance.presentation.model.tablet.ItemHeaderParameterDetailUiModel

class ShopPerformanceAdapterTypeFactory(
    private val shopPerformanceListener: ShopPerformanceListener,
) : BaseAdapterTypeFactory(), ShopPerformanceTypeFactory {

    override fun type(headerShopPerformanceUiModel: HeaderShopPerformanceUiModel): Int {
        return ItemHeaderShopPerformanceViewHolder.LAYOUT
    }

    override fun type(periodDetailPerformanceUiModel: PeriodDetailPerformanceUiModel): Int {
        return PeriodDetailPerformanceViewHolder.LAYOUT
    }

    override fun type(itemDetailPerformanceUiModel: ItemDetailPerformanceUiModel): Int {
        return ItemDetailPerformanceViewHolder.LAYOUT
    }

    override fun type(itemStatusRMUiModel: ItemStatusRMUiModel): Int {
        return ItemStatusRMViewHolder.LAYOUT
    }

    override fun type(itemRMPotentialPMBenefitUIModel: SectionRMPotentialPMBenefitUiModel): Int {
        return CardPotentialPMBenefitViewHolder.LAYOUT
    }

    override fun type(itemStatusPMUiModel: ItemStatusPMUiModel): Int {
        return ItemStatusPMViewHolder.LAYOUT
    }

    override fun type(sectionShopRecommendationUiModel: SectionShopRecommendationUiModel): Int {
        return SectionShopFeatureRecommendationViewHolder.LAYOUT
    }

    override fun type(timerNewSellerUiModel: ItemTimerNewSellerUiModel): Int {
        return ItemTimerNewSellerViewHolder.LAYOUT
    }

    override fun type(sectionFaqUiModel: SectionFaqUiModel): Int {
        return SectionFaqViewHolder.LAYOUT
    }

    override fun type(itemShopPerformanceErrorUiModel: ItemShopPerformanceErrorUiModel): Int {
        return ItemShopPerformanceErrorViewHolder.LAYOUT
    }

    override fun type(itemLevelScoreProjectUiModel: ItemLevelScoreProjectUiModel): Int {
        return ItemLevelScoreProjectViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ShopPerformanceShimmerViewHolder.LAYOUT
    }

    override fun type(sectionRMPMProBenefitUIModel: SectionRMPotentialPMProUiModel): Int {
        return ItemRMPotentialPMProViewHolder.LAYOUT
    }

    override fun type(sectionPMPotentialPMProUiModel: SectionPMPotentialPMProUiModel): Int {
        return ItemPMPotentialPMProViewHolder.LAYOUT
    }

    override fun type(itemStatusPMProUiModel: ItemStatusPMProUiModel): Int {
        return ItemStatusPMProViewHolder.LAYOUT
    }

    override fun type(protectedParameterSectionUiModel: ProtectedParameterSectionUiModel): Int {
        return ItemProtectedParameterSectionViewHolder.LAYOUT
    }

    override fun type(itemReactivatedComebackUiModel: ItemReactivatedComebackUiModel): Int {
        return ItemReactivatedComebackViewHolder.LAYOUT
    }

    override fun type(tickerReactivatedUiModel: TickerReactivatedUiModel): Int {
        return TickerReactivatedViewHolder.LAYOUT
    }

    override fun type(itemHeaderParameterDetailUiModel: ItemHeaderParameterDetailUiModel): Int {
        return ItemHeaderParameterPerformanceViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemHeaderShopPerformanceViewHolder.LAYOUT -> ItemHeaderShopPerformanceViewHolder(
                parent,
                shopPerformanceListener
            )
            PeriodDetailPerformanceViewHolder.LAYOUT -> PeriodDetailPerformanceViewHolder(parent)
            ItemDetailPerformanceViewHolder.LAYOUT -> ItemDetailPerformanceViewHolder(
                parent,
                shopPerformanceListener
            )
            ShopPerformanceShimmerViewHolder.LAYOUT -> ShopPerformanceShimmerViewHolder(parent)
            ItemStatusPMViewHolder.LAYOUT -> ItemStatusPMViewHolder(
                parent,
                shopPerformanceListener
            )
            CardPotentialPMBenefitViewHolder.LAYOUT -> CardPotentialPMBenefitViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemStatusRMViewHolder.LAYOUT -> ItemStatusRMViewHolder(
                parent,
                shopPerformanceListener
            )
            SectionShopFeatureRecommendationViewHolder.LAYOUT -> SectionShopFeatureRecommendationViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemTimerNewSellerViewHolder.LAYOUT -> ItemTimerNewSellerViewHolder(
                parent,
                shopPerformanceListener
            )
            SectionFaqViewHolder.LAYOUT -> SectionFaqViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemShopPerformanceErrorViewHolder.LAYOUT -> ItemShopPerformanceErrorViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemLevelScoreProjectViewHolder.LAYOUT -> ItemLevelScoreProjectViewHolder(parent)
            ItemRMPotentialPMProViewHolder.LAYOUT -> ItemRMPotentialPMProViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemStatusPMProViewHolder.LAYOUT -> ItemStatusPMProViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemPMPotentialPMProViewHolder.LAYOUT -> ItemPMPotentialPMProViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemProtectedParameterSectionViewHolder.LAYOUT -> ItemProtectedParameterSectionViewHolder(
                parent,
                shopPerformanceListener
            )
            TickerReactivatedViewHolder.LAYOUT -> TickerReactivatedViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemReactivatedComebackViewHolder.LAYOUT -> ItemReactivatedComebackViewHolder(
                parent,
                shopPerformanceListener
            )
            ItemHeaderParameterPerformanceViewHolder.LAYOUT -> ItemHeaderParameterPerformanceViewHolder(
                parent,
                shopPerformanceListener
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}