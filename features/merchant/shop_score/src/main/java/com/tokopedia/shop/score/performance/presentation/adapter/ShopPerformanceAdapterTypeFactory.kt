package com.tokopedia.shop.score.performance.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.performance.presentation.model.*

class ShopPerformanceAdapterTypeFactory(
    private val shopPerformanceTypeFactoryListener: ShopPerformanceTypeFactoryListener,
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

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {

            ItemHeaderShopPerformanceViewHolder.LAYOUT -> ItemHeaderShopPerformanceViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.shopPerformanceListener
            )
            PeriodDetailPerformanceViewHolder.LAYOUT -> PeriodDetailPerformanceViewHolder(parent)
            ItemDetailPerformanceViewHolder.LAYOUT -> ItemDetailPerformanceViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.itemShopPerformanceListener
            )
            ShopPerformanceShimmerViewHolder.LAYOUT -> ShopPerformanceShimmerViewHolder(parent)
            ItemStatusPMViewHolder.LAYOUT -> ItemStatusPMViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.itemStatusPowerMerchantListener
            )
            CardPotentialPMBenefitViewHolder.LAYOUT -> CardPotentialPMBenefitViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.itemRegularMerchantListener
            )
            ItemStatusRMViewHolder.LAYOUT -> ItemStatusRMViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.itemPotentialPowerMerchantListener
            )
            SectionShopFeatureRecommendationViewHolder.LAYOUT -> SectionShopFeatureRecommendationViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.itemRecommendationFeatureListener
            )
            ItemTimerNewSellerViewHolder.LAYOUT -> ItemTimerNewSellerViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.itemTimerNewSellerListener
            )
            SectionFaqViewHolder.LAYOUT -> SectionFaqViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.sectionFaqListener
            )
            ItemShopPerformanceErrorViewHolder.LAYOUT -> ItemShopPerformanceErrorViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.globalErrorListener
            )
            ItemLevelScoreProjectViewHolder.LAYOUT -> ItemLevelScoreProjectViewHolder(parent)
            ItemRMPotentialPMProViewHolder.LAYOUT -> ItemRMPotentialPMProViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.potentialPMProListener
            )
            ItemStatusPMProViewHolder.LAYOUT -> ItemStatusPMProViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.itemStatusPowerMerchantProListener
            )
            ItemPMPotentialPMProViewHolder.LAYOUT -> ItemPMPotentialPMProViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.itemPMPotentialPMProListener
            )
            ItemProtectedParameterSectionViewHolder.LAYOUT -> ItemProtectedParameterSectionViewHolder(
                parent,
                shopPerformanceTypeFactoryListener.protectedParameterListener
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}