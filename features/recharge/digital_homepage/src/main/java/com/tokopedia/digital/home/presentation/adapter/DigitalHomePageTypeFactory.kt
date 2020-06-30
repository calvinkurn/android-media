package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.model.*
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.viewholders.DynamicLegoBannerViewHolder
import com.tokopedia.home_component.visitable.*

class DigitalHomePageTypeFactory(
        val listener: OnItemBindListener,
        private val transactionListener: DigitalHomePageTransactionViewHolder.TransactionListener,
        private val isDynamicPage: Boolean = false)
    : BaseAdapterTypeFactory(), HomeComponentTypeFactory {

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

    fun type(bannerModel: RechargeHomepageBannerModel): Int {
        return RechargeHomepageBannerViewHolder.LAYOUT
    }

    fun type(emptyBannerModel: RechargeHomepageBannerEmptyModel): Int {
        return RechargeHomepageBannerEmptyViewHolder.LAYOUT
    }

    fun type(favoriteModel: RechargeHomepageFavoriteModel): Int {
        return RechargeHomepageFavoriteViewHolder.LAYOUT
    }

    fun type(categoryModel: RechargeHomepageCategoryModel): Int {
        return RechargeHomepageCategoryViewHolder.LAYOUT
    }

    fun type(trustMarkModel: RechargeHomepageTrustMarkModel): Int {
        return RechargeHomepageTrustMarkViewHolder.LAYOUT
    }

    fun type(videoHighlightModel: RechargeHomepageVideoHighlightModel): Int {
        return RechargeHomepageVideoHighlightViewHolder.LAYOUT
    }

    fun type(singleBannerModel: RechargeHomepageSingleBannerModel): Int {
        return RechargeHomepageSingleBannerViewHolder.LAYOUT
    }

    fun type(dualBannersModel: RechargeHomepageDualBannersModel): Int {
        return RechargeHomepageDualBannersViewHolder.LAYOUT
    }

    fun type(productCardsModel: RechargeHomepageProductCardsModel): Int {
        return RechargeHomepageProductCardsViewHolder.LAYOUT
    }

    fun type(productBannerModel: RechargeHomepageProductBannerModel): Int {
        return RechargeHomepageProductBannerViewHolder.LAYOUT
    }

    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int {
        return DynamicLegoBannerViewHolder.LAYOUT
    }

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int {
        return 0
    }

    override fun type(reminderWidgetModel: ReminderWidgetModel): Int {
        return 0
    }

    override fun type(mixLeftDataModel: MixLeftDataModel): Int {
        return 0
    }

    override fun type(mixTopDataModel: MixTopDataModel): Int {
        return 0
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return if (isDynamicPage) {
            when (type) {
                // TODO: Finish the rest of viewholders and add them to the list
                RechargeHomepageFavoriteViewHolder.LAYOUT -> RechargeHomepageFavoriteViewHolder(parent, listener)
                RechargeHomepageCategoryViewHolder.LAYOUT -> RechargeHomepageCategoryViewHolder(parent, listener)
                RechargeHomepageTrustMarkViewHolder.LAYOUT -> RechargeHomepageTrustMarkViewHolder(parent, listener)
                RechargeHomepageBannerViewHolder.LAYOUT -> RechargeHomepageBannerViewHolder(parent, listener)
                RechargeHomepageBannerEmptyViewHolder.LAYOUT -> RechargeHomepageBannerEmptyViewHolder(parent)
                RechargeHomepageVideoHighlightViewHolder.LAYOUT -> RechargeHomepageVideoHighlightViewHolder(parent, listener)
                RechargeHomepageSingleBannerViewHolder.LAYOUT -> RechargeHomepageSingleBannerViewHolder(parent, listener)
                RechargeHomepageDualBannersViewHolder.LAYOUT -> RechargeHomepageDualBannersViewHolder(parent, listener)
                DynamicLegoBannerViewHolder.LAYOUT -> DynamicLegoBannerViewHolder(parent, listener, listener)
                RechargeHomepageProductCardsViewHolder.LAYOUT -> RechargeHomepageProductCardsViewHolder(parent, listener)
                RechargeHomepageProductBannerViewHolder.LAYOUT -> RechargeHomepageProductBannerViewHolder(parent, listener)
                else -> super.createViewHolder(parent, type)
            }
        } else {
            when (type) {
                DigitalHomePageBannerViewHolder.LAYOUT -> DigitalHomePageBannerViewHolder(parent, listener)
                DigitalHomePageCategoryViewHolder.LAYOUT -> DigitalHomePageCategoryViewHolder(parent, listener)
                DigitalHomePageTransactionViewHolder.LAYOUT -> DigitalHomePageTransactionViewHolder(parent, transactionListener)
                DigitalHomePageFavoriteViewHolder.LAYOUT -> DigitalHomePageFavoriteViewHolder(parent, listener)
                DigitalHomePageTrustMarkViewHolder.LAYOUT -> DigitalHomePageTrustMarkViewHolder(parent, listener)
                DigitalHomePageNewUserZoneViewHolder.LAYOUT -> DigitalHomePageNewUserZoneViewHolder(parent, listener)
                DigitalHomePageSpotlightViewHolder.LAYOUT -> DigitalHomePageSpotlightViewHolder(parent, listener)
                DigitalHomePageSubscriptionViewHolder.LAYOUT -> DigitalHomePageSubscriptionViewHolder(parent, listener)
                DigitalHomePageRecommendationViewHolder.LAYOUT -> DigitalHomePageRecommendationViewHolder(parent, listener)
                else -> super.createViewHolder(parent, type)
            }
        }
    }

}
