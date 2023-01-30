package com.tokopedia.dilayanitokopedia.home.presentation.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.BannerRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationEmpty
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoadMore
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoading
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou.HomeBannerFeedViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou.HomeRecommendationBannerTopAdsViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou.HomeRecommendationEmptyViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou.HomeRecommendationErrorViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou.HomeRecommendationHeadlineTopAdsViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou.HomeRecommendationItemViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou.HomeRecommendationLoadingMoreViewHolder
import com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou.HomeRecommendationLoadingViewHolder
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener

class HomeRecommendationTypeFactoryImpl(private val topAdsBannerClickListener: TopAdsBannerClickListener) :
    HomeRecommendationTypeFactory {
    override fun type(dataModel: HomeRecommendationItemDataModel): Int {
        return HomeRecommendationItemViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationLoading): Int {
        return HomeRecommendationLoadingViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationEmpty): Int {
        return EmptyViewHolder.LAYOUT
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

    override fun type(dataModel: HomeRecommendationHeadlineTopAdsDataModel): Int {
        return HomeRecommendationHeadlineTopAdsViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, viewType: Int): SmartAbstractViewHolder<*> {
        return when (viewType) {
            HomeRecommendationItemViewHolder.LAYOUT -> HomeRecommendationItemViewHolder(parent, cardInteraction = true)
            HomeRecommendationLoadingViewHolder.LAYOUT -> HomeRecommendationLoadingViewHolder(parent)
            HomeBannerFeedViewHolder.LAYOUT -> HomeBannerFeedViewHolder(parent)
            HomeRecommendationErrorViewHolder.LAYOUT -> HomeRecommendationErrorViewHolder(parent)
            EmptyViewHolder.LAYOUT -> HomeRecommendationEmptyViewHolder(parent)
            HomeRecommendationLoadingMoreViewHolder.LAYOUT -> HomeRecommendationLoadingMoreViewHolder(parent)
            HomeRecommendationBannerTopAdsViewHolder.LAYOUT -> HomeRecommendationBannerTopAdsViewHolder(parent)
            HomeRecommendationHeadlineTopAdsViewHolder.LAYOUT -> HomeRecommendationHeadlineTopAdsViewHolder(
                parent,
                topAdsBannerClickListener
            )
            else -> throw RuntimeException("Home recommendation Layout not supported")
        }
    }
}
