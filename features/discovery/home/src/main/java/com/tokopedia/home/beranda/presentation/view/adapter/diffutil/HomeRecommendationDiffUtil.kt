package com.tokopedia.home.beranda.presentation.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationButtonRetryUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationEmpty
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationError
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoadMore
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationLoading
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecomEntityCardUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

class HomeRecommendationDiffUtil(
    private val typeFactory: HomeRecommendationTypeFactoryImpl
) : DiffUtil.ItemCallback<Visitable<HomeRecommendationTypeFactoryImpl>>() {

    override fun getChangePayload(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Any? {
        return Pair(oldItem, newItem)
    }

    override fun areItemsTheSame(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Boolean {
        return when {
            oldItem is BannerRecommendationDataModel && newItem is BannerRecommendationDataModel -> {
                oldItem.id == newItem.id
            }
            oldItem is HomeRecommendationBannerTopAdsOldDataModel && newItem is HomeRecommendationBannerTopAdsOldDataModel -> {
                oldItem.topAdsImageViewModel?.bannerId == newItem.topAdsImageViewModel?.bannerId
            }
            oldItem is HomeRecommendationBannerTopAdsUiModel && newItem is HomeRecommendationBannerTopAdsUiModel -> {
                oldItem.topAdsImageViewModel?.bannerId == newItem.topAdsImageViewModel?.bannerId
            }
            oldItem is HomeRecommendationHeadlineTopAdsDataModel && newItem is HomeRecommendationHeadlineTopAdsDataModel -> {
                oldItem.headlineAds.data.firstOrNull()?.id == newItem.headlineAds.data.firstOrNull()?.id
            }
            oldItem is HomeRecommendationItemDataModel && newItem is HomeRecommendationItemDataModel -> {
                oldItem.recommendationProductItem.id == newItem.recommendationProductItem.id
            }
            oldItem is HomeRecommendationPlayWidgetUiModel && newItem is HomeRecommendationPlayWidgetUiModel -> {
                oldItem.playVideoWidgetUiModel.id == newItem.playVideoWidgetUiModel.id
            }

            oldItem is HomeRecommendationLoading && newItem is HomeRecommendationLoading -> {
                oldItem.id == newItem.id
            }

            oldItem is HomeRecommendationLoadMore && newItem is HomeRecommendationLoadMore -> {
                oldItem.id == newItem.id
            }

            oldItem is HomeRecommendationError && newItem is HomeRecommendationError -> {
                oldItem.id == newItem.id
            }

            oldItem is HomeRecommendationEmpty && newItem is HomeRecommendationEmpty -> {
                oldItem.id == newItem.id
            }

            oldItem is HomeRecommendationButtonRetryUiModel && newItem is HomeRecommendationButtonRetryUiModel -> {
                oldItem.id == newItem.id
            }

            oldItem is RecomEntityCardUiModel && newItem is RecomEntityCardUiModel -> {
                oldItem.id == newItem.id
            }

            else -> {
                return oldItem.type(typeFactory) == newItem.type(typeFactory)
            }
        }
    }

    override fun areContentsTheSame(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Boolean {
        return oldItem.equals(newItem)
    }
}
