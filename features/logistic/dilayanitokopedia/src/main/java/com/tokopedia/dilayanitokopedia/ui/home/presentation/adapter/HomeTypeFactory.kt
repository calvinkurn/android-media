package com.tokopedia.dilayanitokopedia.ui.home.presentation.adapter

import com.tokopedia.dilayanitokopedia.ui.home.presentation.datamodel.HomeLoadingMoreModel
import com.tokopedia.dilayanitokopedia.ui.home.presentation.datamodel.HomeRecommendationFeedDataModel
import com.tokopedia.dilayanitokopedia.ui.home.presentation.uimodel.HomeLoadingStateUiModel

interface HomeTypeFactory {

    fun type(uiModel: HomeLoadingStateUiModel): Int

    fun type(uiModel: HomeRecommendationFeedDataModel): Int

    fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int
}
