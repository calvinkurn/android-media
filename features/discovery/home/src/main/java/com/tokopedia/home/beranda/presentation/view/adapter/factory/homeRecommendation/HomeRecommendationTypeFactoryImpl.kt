package com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil.getLayout
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationVideoWidgetManager
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener

class HomeRecommendationTypeFactoryImpl(
    private val topAdsBannerClickListener: TopAdsBannerClickListener,
    private val homeRecommendationListener: HomeRecommendationListener,
    private val homeRecommendationVideoWidgetManager: HomeRecommendationVideoWidgetManager
) : BaseAdapterTypeFactory(), HomeRecommendationTypeFactory {
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

    override fun type(dataModel: HomeRecommendationBannerTopAdsOldDataModel): Int {
        return HomeRecommendationBannerTopAdsOldViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationHeadlineTopAdsDataModel): Int {
        return HomeRecommendationHeadlineTopAdsViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationPlayWidgetUiModel): Int {
        return HomeRecommendationPlayWidgetViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationBannerTopAdsUiModel): Int {
        return HomeRecommendationBannerTopAdsViewHolder.LAYOUT
    }

    override fun type(dataModel: HomeRecommendationButtonRetryUiModel): Int {
        return HomeRecommendationButtonRetryViewHolder.LAYOUT
    }

    override fun type(uiModel: RecomEntityCardUiModel): Int {
        return RecomEntityCardViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            HomeRecommendationItemGridViewHolder.LAYOUT -> HomeRecommendationItemGridViewHolder(
                parent,
                homeRecommendationListener
            )

            RecomEntityCardViewHolder.LAYOUT -> RecomEntityCardViewHolder(
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

            HomeRecommendationBannerTopAdsOldViewHolder.LAYOUT -> HomeRecommendationBannerTopAdsOldViewHolder(
                parent,
                homeRecommendationListener
            )

            HomeRecommendationHeadlineTopAdsViewHolder.LAYOUT -> HomeRecommendationHeadlineTopAdsViewHolder(
                parent,
                topAdsBannerClickListener
            )

            HomeRecommendationPlayWidgetViewHolder.LAYOUT -> HomeRecommendationPlayWidgetViewHolder(parent, homeRecommendationVideoWidgetManager, homeRecommendationListener)

            HomeRecommendationBannerTopAdsViewHolder.LAYOUT -> HomeRecommendationBannerTopAdsViewHolder(parent, homeRecommendationListener)

            HomeRecommendationButtonRetryViewHolder.LAYOUT -> HomeRecommendationButtonRetryViewHolder(parent, homeRecommendationListener)

            else -> super.createViewHolder(parent, type)
        }
    }
}
