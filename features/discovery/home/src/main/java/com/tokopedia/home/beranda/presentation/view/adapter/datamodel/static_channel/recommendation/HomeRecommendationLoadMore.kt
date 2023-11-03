package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

data class HomeRecommendationLoadMore(
    val id: String = LOADING_ID
) : HomeRecommendationVisitable {
    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
    companion object {
        private const val LOADING_ID = "RECOMMENDATION_LOADING_MORE"
    }
}
