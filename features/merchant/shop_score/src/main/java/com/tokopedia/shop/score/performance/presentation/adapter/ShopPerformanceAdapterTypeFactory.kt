package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemDetailPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemHeaderShopPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemLevelScoreProjectViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemPMPotentialPMProViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemProtectedParameterSectionViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemReactivatedComebackViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemShopPerformanceErrorViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemStatusRMViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ItemTimerNewSellerViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.PeriodDetailPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.SectionFaqViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.SectionShopFeatureRecommendationViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.ShopPerformanceShimmerViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.TickerReactivatedViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet.ItemHeaderParameterPerformanceViewHolder
import com.tokopedia.shop.score.performance.presentation.model.HeaderShopPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemLevelScoreProjectUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemReactivatedComebackUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemShopPerformanceErrorUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusRMUiModel
import com.tokopedia.shop.score.performance.presentation.model.ItemTimerNewSellerUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ProtectedParameterSectionUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionPMPotentialPMProUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionShopRecommendationUiModel
import com.tokopedia.shop.score.performance.presentation.model.TickerReactivatedUiModel
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

    override fun type(sectionPMPotentialPMProUiModel: SectionPMPotentialPMProUiModel): Int {
        return ItemPMPotentialPMProViewHolder.LAYOUT
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
