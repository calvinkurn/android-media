package com.tokopedia.topads.dashboard.recommendation.data.model.local

sealed class TopAdsListAllInsightState<out T : Any> {
    data class Success<out T : Any>(val data: T) : TopAdsListAllInsightState<T>()
    data class Fail(val throwable: Throwable) : TopAdsListAllInsightState<Nothing>()
    data class Loading(val type: Int) : TopAdsListAllInsightState<Nothing>()
}
