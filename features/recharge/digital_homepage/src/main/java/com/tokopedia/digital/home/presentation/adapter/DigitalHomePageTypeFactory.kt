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
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel

class DigitalHomePageTypeFactory(val listener: OnItemBindListener,
                                 val transactionListener: DigitalHomePageTransactionViewHolder.TransactionListener?)
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
        return RechargeHomepageBannerViewHolder.LAYOUT_EMPTY
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

    fun type(productBannerModel: RechargeHomepageProductBannerModel): Int {
        return RechargeHomepageProductBannerViewHolder.LAYOUT
    }

    override fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int {
        return DynamicLegoBannerViewHolder.LAYOUT
    }

    override fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int {
        return 0
    }

    override fun type(mixLeftDataModel: MixLeftDataModel): Int {
        return 0
    }

    override fun type(mixTopDataModel: MixTopDataModel): Int {
        return 0
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            // TODO: Finish the rest of viewholders and add them to the list
            RechargeHomepageFavoriteViewHolder.LAYOUT -> RechargeHomepageFavoriteViewHolder(parent, listener)
            RechargeHomepageCategoryViewHolder.LAYOUT -> RechargeHomepageCategoryViewHolder(parent, listener)
            RechargeHomepageTrustMarkViewHolder.LAYOUT -> RechargeHomepageTrustMarkViewHolder(parent, listener)
            RechargeHomepageBannerViewHolder.LAYOUT -> RechargeHomepageBannerViewHolder(parent, listener)
            RechargeHomepageBannerViewHolder.LAYOUT_EMPTY -> RechargeHomepageBannerViewHolder(parent, listener, true)
            RechargeHomepageVideoHighlightViewHolder.LAYOUT -> RechargeHomepageVideoHighlightViewHolder(parent, listener)
            DynamicLegoBannerViewHolder.LAYOUT -> DynamicLegoBannerViewHolder(parent, listener, listener)
            RechargeHomepageProductBannerViewHolder.LAYOUT -> RechargeHomepageProductBannerViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
