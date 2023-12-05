package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

data class HomeRecommendationError(val throwable: Throwable? = null, val id: String = ID) :
    BaseHomeRecommendationVisitable {
    override fun areItemsTheSame(other: Any): Boolean {
        return other is HomeRecommendationError && id == other.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return other == this
    }

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    companion object {
        private const val ID = "RECOMMENDATION_ERROR"
    }
}
