package com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation

import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.*
import com.tokopedia.smart_recycler_helper.SmartTypeFactory

interface HomeRecommendationTypeFactory: SmartTypeFactory{
    fun type(dataModel: HomeRecommendationLoading): Int
    fun type(dataModel: HomeRecommendationEmpty): Int
    fun type(dataModel: HomeRecommendationError): Int
    fun type(dataModel: HomeRecommendationLoadMore): Int
    fun type(dataModel: HomeRecommendationItemDataModel): Int
    fun type(dataModel: BannerRecommendationDataModel): Int
    fun type(dataModel: HomeRecommendationBannerTopAdsDataModel): Int
    fun type(dataModel: HomeRecommendationHeadlineTopAdsDataModel): Int
}