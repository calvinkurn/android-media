package com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.*
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder

class HomeRecommendationTypeFactoryImpl : HomeRecommendationTypeFactory {
    override fun type(dataModel: HomeRecommendationItemDataModel): Int {
        return HomeRecommendationItemViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationLoading): Int {
        return HomeRecommendationLoadingViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationEmpty): Int {
        return EmptyViewHolder.LAYOUT;
    }

    override fun type(dataModel: HomeRecommendationError): Int {
        return HomeRecommendationErrorViewHolder.LAYOUT
    }

    override fun type(dataModel: BannerRecommendationDataModel): Int {
        return HomeBannerFeedViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationLoadMore): Int {
        return HomeRecommendationLoadingMoreViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationBannerTopAdsDataModel): Int {
        return HomeRecommendationBannerTopAdsViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, viewType: Int): SmartAbstractViewHolder<*> {
        return when (viewType) {
            HomeRecommendationItemViewHolder.LAYOUT -> HomeRecommendationItemViewHolder(parent)
            HomeRecommendationLoadingViewHolder.LAYOUT -> HomeRecommendationLoadingViewHolder(parent)
            HomeBannerFeedViewHolder.LAYOUT -> HomeBannerFeedViewHolder(parent)
            HomeRecommendationErrorViewHolder.LAYOUT -> HomeRecommendationErrorViewHolder(parent)
            EmptyViewHolder.LAYOUT -> HomeRecommendationEmptyViewHolder(parent)
            HomeRecommendationLoadingMoreViewHolder.LAYOUT -> HomeRecommendationLoadingMoreViewHolder(parent)
            HomeRecommendationBannerTopAdsViewHolder.LAYOUT -> HomeRecommendationBannerTopAdsViewHolder(parent)
            else -> throw RuntimeException("Home recommendation Layout not supported")
        }
    }

}