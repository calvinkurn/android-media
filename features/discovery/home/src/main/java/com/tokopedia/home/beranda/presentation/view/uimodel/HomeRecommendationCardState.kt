package com.tokopedia.home.beranda.presentation.view.uimodel

sealed class HomeRecommendationCardState<out T> {
    data class Loading<T>(val data: T) : HomeRecommendationCardState<T>()
    data class LoadingMore<T>(val data: T) : HomeRecommendationCardState<T>()
    data class EmptyData<T>(val data: T) : HomeRecommendationCardState<T>()

    data class Success<T>(val data: T) : HomeRecommendationCardState<T>()

    data class Fail<T>(
        val data: T,
        val throwable: Throwable
    ) : HomeRecommendationCardState<T>()

    data class FailNextPage<T>(
        val data: T,
        val throwable: Throwable
    ) : HomeRecommendationCardState<T>()
}
