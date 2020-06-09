package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener

class DigitalHomePageTypeFactory(val onItemBindListener: OnItemBindListener,
                                 val transactionListener: DigitalHomePageTransactionViewHolder.TransactionListener?)
    : BaseAdapterTypeFactory() {

    fun type(digitalHomePageBannerModel: DigitalHomePageBannerModel): Int {
        return DigitalHomePageBannerViewHolder.LAYOUT
    }

    fun type(digitalHomePageCategoryModel: DigitalHomePageCategoryModel): Int {
        return DigitalHomePageCategoryViewHolder.LAYOUT
    }

    fun type(digitalHomePageTransactionModel: DigitalHomePageTransactionModel): Int {
        return DigitalHomePageTransactionViewHolder.LAYOUT
    }

    fun type(digitalHomePageFavoritesModel: DigitalHomePageFavoritesModel): Int {
        return DigitalHomePageFavoriteViewHolder.LAYOUT
    }

    fun type(digitalHomePageTrustMarkModel: DigitalHomePageTrustMarkModel): Int {
        return DigitalHomePageTrustMarkViewHolder.LAYOUT
    }

    fun type(digitalHomePageNewUserZoneModel: DigitalHomePageNewUserZoneModel): Int {
        return DigitalHomePageNewUserZoneViewHolder.LAYOUT
    }

    fun type(digitalHomePageSpotlightModel: DigitalHomePageSpotlightModel): Int {
        return DigitalHomePageSpotlightViewHolder.LAYOUT
    }

    fun type(digitalHomePageSubscriptionModel: DigitalHomePageSubscriptionModel): Int {
        return DigitalHomePageSubscriptionViewHolder.LAYOUT
    }

    fun type(digitalHomePageRecommendationModel: DigitalHomePageRecommendationModel): Int {
        return DigitalHomePageRecommendationViewHolder.LAYOUT
    }

    fun type(topBannerModel: RechargeHomepageTopBannerModel): Int {
        return RechargeHomepageTopBannerViewHolder.LAYOUT
    }

    fun type(topIconsModel: RechargeHomepageTopBannerEmptyModel): Int {
        return RechargeHomepageTopBannerViewHolder.LAYOUT_EMPTY
    }

    fun type(topIconsModel: RechargeHomepageTopIconsModel): Int {
        return RechargeHomepageFavoriteViewHolder.LAYOUT
    }

    fun type(dynamicIconsModel: RechargeHomepageDynamicIconsModel): Int {
        return RechargeHomepageCategoryViewHolder.LAYOUT
    }

    fun type(dualIconsModel: RechargeHomepageDualIconsModel): Int {
        return RechargeHomepageTrustMarkViewHolder.LAYOUT
    }

    fun type(urgencyWidgetModel: RechargeHomepageUrgencyWidgetModel): Int {
        return RechargeHomepageUrgencyWidgetViewHolder.LAYOUT
    }

    fun type(videoHighlightModel: RechargeHomepageVideoHighlightModel): Int {
        return RechargeHomepageVideoHighlightViewHolder.LAYOUT
    }

    fun type(videoHighlightsModel: RechargeHomepageVideoHighlightsModel): Int {
        return RechargeHomepageVideoHighlightsViewHolder.LAYOUT
    }

    fun type(singleBannerModel: RechargeHomepageSingleBannerModel): Int {
        return RechargeHomepageSingleBannerViewHolder.LAYOUT
    }

    fun type(countdownSingleBannerModel: RechargeHomepageCountdownSingleBannerModel): Int {
        return RechargeHomepageSingleViewHolder.LAYOUT_COUNTDOWN
    }

    fun type(dualBannersModel: RechargeHomepageDualBannersModel): Int {
        return RechargeHomepageDualBannersViewHolder.LAYOUT
    }

    fun type(legoBannersModel: RechargeHomepageLegoBannersModel): Int {
        return RechargeHomepageLegoBannersViewHolder.LAYOUT
    }

    fun type(productCardRowModel: RechargeHomepageProductCardRowModel): Int {
        return RechargeHomepageProductCardRowViewHolder.LAYOUT
    }

    fun type(countdownProductBannerModel: RechargeHomepageCountdownProductBannerModel): Int {
        return RechargeHomepageCountdownProductBannerViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            DigitalHomePageBannerViewHolder.LAYOUT -> DigitalHomePageBannerViewHolder(parent, onItemBindListener)
            DigitalHomePageCategoryViewHolder.LAYOUT -> DigitalHomePageCategoryViewHolder(parent, onItemBindListener)
            DigitalHomePageTransactionViewHolder.LAYOUT -> DigitalHomePageTransactionViewHolder(parent, transactionListener)
            DigitalHomePageFavoriteViewHolder.LAYOUT -> DigitalHomePageFavoriteViewHolder(parent, onItemBindListener)
            DigitalHomePageTrustMarkViewHolder.LAYOUT -> DigitalHomePageTrustMarkViewHolder(parent, onItemBindListener)
            DigitalHomePageNewUserZoneViewHolder.LAYOUT -> DigitalHomePageNewUserZoneViewHolder(parent, onItemBindListener)
            DigitalHomePageSpotlightViewHolder.LAYOUT -> DigitalHomePageSpotlightViewHolder(parent, onItemBindListener)
            DigitalHomePageSubscriptionViewHolder.LAYOUT -> DigitalHomePageSubscriptionViewHolder(parent, onItemBindListener)
            DigitalHomePageRecommendationViewHolder.LAYOUT -> DigitalHomePageRecommendationViewHolder(parent, onItemBindListener)

            RechargeHomepageCategoryViewHolder.LAYOUT -> RechargeHomepageCategoryViewHolder(parent, onItemBindListener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
