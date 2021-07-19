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
                                        private val sectionFaqListener: SectionFaqListener,
                                        private val globalErrorListener: GlobalErrorListener,
                                        private val itemRegularMerchantListener: ItemRegularMerchantListener,
                                        private val potentialPMProListener: ItemRMPotentialPMProListener,
                                        private val itemStatusPowerMerchantProListener: ItemStatusPowerMerchantProListener
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

    override fun type(sectionRMPMProBenefitUIModel: SectionRMPotentialPMProUiModel): Int {
        return ItemRMPotentialPMProViewHolder.LAYOUT
    }

    override fun type(sectionPMPotentialPMProUiModel: SectionPMPotentialPMProUiModel): Int {
        return ItemPMPotentialPMProViewHolder.LAYOUT
    }

    override fun type(itemStatusPMProUiModel: ItemStatusPMProUiModel): Int {
        return ItemStatusPMProViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemHeaderShopPerformanceViewHolder.LAYOUT -> ItemHeaderShopPerformanceViewHolder(parent, shopPerformanceListener)
            PeriodDetailPerformanceViewHolder.LAYOUT -> PeriodDetailPerformanceViewHolder(parent)
            ItemDetailPerformanceViewHolder.LAYOUT -> ItemDetailPerformanceViewHolder(parent, itemShopPerformanceListener)
            ShopPerformanceShimmerViewHolder.LAYOUT -> ShopPerformanceShimmerViewHolder(parent)
            ItemStatusPMViewHolder.LAYOUT -> ItemStatusPMViewHolder(parent, itemStatusPowerMerchantListener)
            CardPotentialPMBenefitViewHolder.LAYOUT -> CardPotentialPMBenefitViewHolder(parent, itemRegularMerchantListener)
            ItemStatusRMViewHolder.LAYOUT -> ItemStatusRMViewHolder(parent, itemPotentialPowerMerchantListener)
            SectionShopFeatureRecommendationViewHolder.LAYOUT -> SectionShopFeatureRecommendationViewHolder(parent, itemRecommendationFeatureListener)
            ItemTimerNewSellerViewHolder.LAYOUT -> ItemTimerNewSellerViewHolder(parent, itemTimerNewSellerListener)
            SectionFaqViewHolder.LAYOUT -> SectionFaqViewHolder(parent, sectionFaqListener)
            ItemShopPerformanceErrorViewHolder.LAYOUT -> ItemShopPerformanceErrorViewHolder(parent, globalErrorListener)
            ItemLevelScoreProjectViewHolder.LAYOUT -> ItemLevelScoreProjectViewHolder(parent)
            ItemRMPotentialPMProViewHolder.LAYOUT -> ItemRMPotentialPMProViewHolder(parent, potentialPMProListener)
            ItemStatusPMProViewHolder.LAYOUT -> ItemStatusPMProViewHolder(parent, itemStatusPowerMerchantProListener)
            else -> return super.createViewHolder(parent, type)
        }
    }
}