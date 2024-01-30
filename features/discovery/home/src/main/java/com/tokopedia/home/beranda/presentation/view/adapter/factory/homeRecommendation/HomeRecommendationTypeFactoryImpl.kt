package com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationUtil.getLayout
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.*
import com.tokopedia.home.beranda.presentation.view.helper.HomeRecommendationVideoWidgetManager
import com.tokopedia.recommendation_widget_common.widget.foryou.GlobalRecomListener

class HomeRecommendationTypeFactoryImpl(
    private val listener: GlobalRecomListener,
    private val homeRecommendationVideoWidgetManager: HomeRecommendationVideoWidgetManager
) : BaseAdapterTypeFactory(), HomeRecommendationTypeFactory {
    override fun type(dataModel: RecomEntityCardUiModel): Int {
        return RecomEntityCardViewHolder.LAYOUT
    }
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

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            HomeRecommendationItemGridViewHolder.LAYOUT -> HomeRecommendationItemGridViewHolder(
                parent,
                listener
            )

            RecomEntityCardViewHolder.LAYOUT -> RecomEntityCardViewHolder(parent, listener)

            HomeRecommendationItemListViewHolder.LAYOUT -> HomeRecommendationItemListViewHolder(
                parent,
                listener
            )

            HomeRecommendationLoadingViewHolder.LAYOUT -> HomeRecommendationLoadingViewHolder(parent)
            HomeBannerFeedViewHolder.LAYOUT -> HomeBannerFeedViewHolder(
                parent,
                listener
            )

            HomeRecommendationErrorViewHolder.LAYOUT -> HomeRecommendationErrorViewHolder(
                parent,
                listener
            )

            EmptyViewHolder.LAYOUT -> HomeRecommendationEmptyViewHolder(parent)

            HomeRecommendationLoadingMoreViewHolder.LAYOUT -> HomeRecommendationLoadingMoreViewHolder(
                parent
            )

            HomeRecommendationBannerTopAdsOldViewHolder.LAYOUT -> HomeRecommendationBannerTopAdsOldViewHolder(
                parent,
                listener
            )

            HomeRecommendationHeadlineTopAdsViewHolder.LAYOUT -> HomeRecommendationHeadlineTopAdsViewHolder(
                parent,
                listener
            )

            HomeRecommendationPlayWidgetViewHolder.LAYOUT -> HomeRecommendationPlayWidgetViewHolder(parent, homeRecommendationVideoWidgetManager, listener)

            HomeRecommendationBannerTopAdsViewHolder.LAYOUT -> HomeRecommendationBannerTopAdsViewHolder(parent, listener)

            HomeRecommendationButtonRetryViewHolder.LAYOUT -> HomeRecommendationButtonRetryViewHolder(parent, listener)

            else -> super.createViewHolder(parent, type)
        }
    }
}
