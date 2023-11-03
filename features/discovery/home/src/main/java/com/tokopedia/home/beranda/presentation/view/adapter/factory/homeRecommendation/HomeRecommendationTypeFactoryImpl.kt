package com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil.getLayout
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.*
import com.tokopedia.recommendation_widget_common.widget.entrypointcard.model.RecomEntryPointCardUiModel
import com.tokopedia.recommendation_widget_common.widget.entrypointcard.typefactory.RecomEntryPointCardTypeFactory
import com.tokopedia.recommendation_widget_common.widget.entrypointcard.viewholder.RecomEntryPointCardViewHolder
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener

class HomeRecommendationTypeFactoryImpl(
    private val topAdsBannerClickListener: TopAdsBannerClickListener,
    private val homeRecommendationListener: HomeRecommendationListener
) : BaseAdapterTypeFactory(), HomeRecommendationTypeFactory, RecomEntryPointCardTypeFactory {
    override fun type(dataModel: HomeRecommendationItemDataModel): Int {
        return dataModel.getLayout()
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

    override fun type(uiModel: RecomEntryPointCardUiModel): Int {
        return RecomEntryPointCardViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            HomeRecommendationItemGridViewHolder.LAYOUT -> HomeRecommendationItemGridViewHolder(
                parent,
                homeRecommendationListener
            )

            RecomEntryPointCardViewHolder.LAYOUT -> RecomEntryPointCardViewHolder(
                parent,
                homeRecommendationListener
            )

            HomeRecommendationItemListViewHolder.LAYOUT -> HomeRecommendationItemListViewHolder(
                parent,
                homeRecommendationListener
            )

            HomeRecommendationLoadingViewHolder.LAYOUT -> HomeRecommendationLoadingViewHolder(parent)
            HomeBannerFeedViewHolder.LAYOUT -> HomeBannerFeedViewHolder(
                parent,
                homeRecommendationListener
            )

            HomeRecommendationErrorViewHolder.LAYOUT -> HomeRecommendationErrorViewHolder(
                parent,
                homeRecommendationListener
            )

            EmptyViewHolder.LAYOUT -> HomeRecommendationEmptyViewHolder(parent)

            HomeRecommendationLoadingMoreViewHolder.LAYOUT -> HomeRecommendationLoadingMoreViewHolder(
                parent
            )

            HomeRecommendationBannerTopAdsViewHolder.LAYOUT -> HomeRecommendationBannerTopAdsViewHolder(
                parent,
                homeRecommendationListener
            )

            HomeRecommendationHeadlineTopAdsViewHolder.LAYOUT -> HomeRecommendationHeadlineTopAdsViewHolder(
                parent,
                topAdsBannerClickListener
            )

            else -> super.createViewHolder(parent, type)
        }
    }
}
