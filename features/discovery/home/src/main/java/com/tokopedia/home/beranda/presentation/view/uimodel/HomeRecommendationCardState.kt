package com.tokopedia.home.beranda.presentation.view.uimodel

import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel

sealed class HomeRecommendationCardState {
    data class Loading(val homeRecommendationLoading: List<HomeRecommendationVisitable>) : HomeRecommendationCardState()

    data class EmptyData(val homeRecommendationLoading: List<HomeRecommendationVisitable>) : HomeRecommendationCardState()
    data class LoadingMore(val homeRecommendationLoadingMore: List<HomeRecommendationVisitable>) : HomeRecommendationCardState()
    data class Success(val data: HomeRecommendationDataModel) : HomeRecommendationCardState()
    data class Fail(val throwable: Throwable) : HomeRecommendationCardState()
}
