package com.tokopedia.dilayanitokopedia.common.view.adapter.typefactory

import com.tokopedia.dilayanitokopedia.ui.home.adapter.uimodel.HomeLoadingMoreModel
import com.tokopedia.dilayanitokopedia.ui.home.adapter.uimodel.HomeRecommendationFeedDataModel
import com.tokopedia.dilayanitokopedia.ui.home.uimodel.HomeLoadingStateUiModel

interface HomeTypeFactory {

    fun type(uiModel: HomeLoadingStateUiModel): Int

    fun type(uiModel: HomeRecommendationFeedDataModel): Int

    fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int
}
