package com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*

interface HomeRecommendationTypeFactory {
    fun type(dataModel: HomeRecommendationLoading): Int
    fun type(dataModel: HomeRecommendationEmpty): Int
    fun type(dataModel: HomeRecommendationError): Int
    fun type(dataModel: HomeRecommendationLoadMore): Int
    fun type(dataModel: HomeRecommendationItemDataModel): Int
    fun type(dataModel: BannerRecommendationDataModel): Int
    fun type(dataModel: HomeRecommendationBannerTopAdsOldDataModel): Int
    fun type(dataModel: HomeRecommendationHeadlineTopAdsDataModel): Int
    fun type(dataModel: HomeRecommendationPlayWidgetUiModel): Int
    fun type(dataModel: HomeRecommendationBannerTopAdsUiModel): Int
    fun type(dataModel: HomeRecommendationButtonRetryUiModel): Int

    fun type(dataModel: RecomEntityCardUiModel): Int
}
