package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.performance.presentation.model.*

class ShopPerformanceAdapterTypeFactory(private val shopPerformanceListener: ShopPerformanceListener,
                                        private val itemShopPerformanceListener: ItemShopPerformanceListener,
                                        private val itemPotentialPowerMerchantListener: ItemPotentialRegularMerchantListener,
                                        private val itemRecommendationFeatureListener: ItemRecommendationFeatureListener,
                                        private val itemStatusPowerMerchantListener: ItemStatusPowerMerchantListener,
                                        private val itemTimerNewSellerListener: ItemTimerNewSellerListener,
                                        private val itemHeaderShopPerformanceListener: ItemHeaderShopPerformanceListener,
                                        private val sectionFaqListener: SectionFaqListener,
                                        private val globalErrorListener: GlobalErrorListener,
                                        private val periodDetailPerformanceListener: PeriodDetailPerformanceListener,
                                        private val cardPotentialPMBenefitListener: CardPotentialPMBenefitListener
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

    override fun type(transitionPeriodReliefUiModel: TransitionPeriodReliefUiModel): Int {
        return TransitionPeriodReliefViewHolder.LAYOUT
    }

    override fun type(itemStatusRMUiModel: ItemStatusRMUiModel): Int {
        return ItemStatusRMViewHolder.LAYOUT
    }

    override fun type(itemPotentialPMBenefitUIModel: SectionPotentialPMBenefitUiModel): Int {
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

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemHeaderShopPerformanceViewHolder.LAYOUT -> ItemHeaderShopPerformanceViewHolder(parent, shopPerformanceListener, itemHeaderShopPerformanceListener)
            PeriodDetailPerformanceViewHolder.LAYOUT -> PeriodDetailPerformanceViewHolder(parent, periodDetailPerformanceListener)
            ItemDetailPerformanceViewHolder.LAYOUT -> ItemDetailPerformanceViewHolder(parent, itemShopPerformanceListener)
            ShopPerformanceShimmerViewHolder.LAYOUT -> ShopPerformanceShimmerViewHolder(parent)
            TransitionPeriodReliefViewHolder.LAYOUT -> TransitionPeriodReliefViewHolder(parent)
            ItemStatusPMViewHolder.LAYOUT -> ItemStatusPMViewHolder(parent, itemStatusPowerMerchantListener)
            CardPotentialPMBenefitViewHolder.LAYOUT -> CardPotentialPMBenefitViewHolder(parent, cardPotentialPMBenefitListener)
            ItemStatusRMViewHolder.LAYOUT -> ItemStatusRMViewHolder(parent, itemPotentialPowerMerchantListener)
            SectionShopFeatureRecommendationViewHolder.LAYOUT -> SectionShopFeatureRecommendationViewHolder(parent, itemRecommendationFeatureListener)
            ItemTimerNewSellerViewHolder.LAYOUT -> ItemTimerNewSellerViewHolder(parent, itemTimerNewSellerListener)
            SectionFaqViewHolder.LAYOUT -> SectionFaqViewHolder(parent, sectionFaqListener)
            ItemShopPerformanceErrorViewHolder.LAYOUT -> ItemShopPerformanceErrorViewHolder(parent, globalErrorListener)
            ItemLevelScoreProjectViewHolder.LAYOUT -> ItemLevelScoreProjectViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}