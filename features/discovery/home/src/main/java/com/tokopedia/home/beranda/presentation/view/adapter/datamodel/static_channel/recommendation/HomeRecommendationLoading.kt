package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

data class HomeRecommendationLoading(
    val id: String = LOADING_ID
) : BaseHomeRecommendationVisitable {
    override fun areItemsTheSame(other: Any): Boolean {
        return other is HomeRecommendationLoading && other.id == id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        // always bind, don't have the specific field
        return true
    }

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    companion object {
        private const val LOADING_ID = "RECOMMENDATION_LOADING"
    }
}
