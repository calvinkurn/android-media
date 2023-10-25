package com.tokopedia.home.beranda.presentation.view.uimodel

import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel

sealed class HomeRecommendationCardState {
    data class Loading(val loadingList: List<HomeRecommendationVisitable>) :
        HomeRecommendationCardState()

    data class LoadingMore(val loadingMoreList: List<HomeRecommendationVisitable>) :
        HomeRecommendationCardState()

    data class EmptyData(val emptyList: List<HomeRecommendationVisitable>) :
        HomeRecommendationCardState()

    data class Success(val data: HomeRecommendationDataModel) : HomeRecommendationCardState()
    data class SuccessNextPage(val data: HomeRecommendationDataModel) :
        HomeRecommendationCardState()

    data class Fail(
        val errorList: List<HomeRecommendationVisitable>,
        val throwable: Throwable
    ) : HomeRecommendationCardState()

    data class FailNextPage(
        val existingList: List<HomeRecommendationVisitable>,
        val throwable: Throwable
    ) : HomeRecommendationCardState()
}
