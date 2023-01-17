package com.tokopedia.dilayanitokopedia.home.presentation.factory

import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.BannerRecommendationDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationEmpty
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoadMore
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationLoading
import com.tokopedia.smart_recycler_helper.SmartTypeFactory

interface HomeRecommendationTypeFactory : SmartTypeFactory {
    fun type(dataModel: HomeRecommendationLoading): Int
    fun type(dataModel: HomeRecommendationEmpty): Int
    fun type(dataModel: HomeRecommendationError): Int
    fun type(dataModel: HomeRecommendationLoadMore): Int
    fun type(dataModel: HomeRecommendationItemDataModel): Int
    fun type(dataModel: BannerRecommendationDataModel): Int
    fun type(dataModel: HomeRecommendationBannerTopAdsDataModel): Int
    fun type(dataModel: HomeRecommendationHeadlineTopAdsDataModel): Int
}
