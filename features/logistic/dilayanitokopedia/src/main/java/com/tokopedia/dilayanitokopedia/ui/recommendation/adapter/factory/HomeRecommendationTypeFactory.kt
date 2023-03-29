package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.factory

import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.BannerRecommendationDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationEmpty
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationError
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationHeadlineTopAdsDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationItemDataModel
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationLoadMore
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationLoading
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
