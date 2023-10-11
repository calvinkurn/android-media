package com.tokopedia.navigation_common.listener

interface HomeScrollViewListener {
    fun onScrollToRecommendationForYou()

    fun onScrollToHomeHeader()

    fun getRecommendationForYouIndex(): Int?
}
