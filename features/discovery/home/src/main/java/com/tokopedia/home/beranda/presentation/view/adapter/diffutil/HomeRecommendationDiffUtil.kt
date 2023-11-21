package com.tokopedia.home.beranda.presentation.view.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationPlayWidgetUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

class HomeRecommendationDiffUtil(
    private val typeFactory: HomeRecommendationTypeFactoryImpl
) : DiffUtil.ItemCallback<Visitable<HomeRecommendationTypeFactoryImpl>>() {

    override fun getChangePayload(
        oldItem: Visitable<HomeRecommendationTypeFactoryImpl>,
        newItem: Visitable<HomeRecommendationTypeFactoryImpl>
    ): Any? {
        return if (oldItem.type(typeFactory) == newItem.type(typeFactory)) {
            Pair(oldItem, newItem)
        } else {
            null
        }
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
            else -> {
                oldItem.type(typeFactory) == newItem.type(typeFactory)
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
