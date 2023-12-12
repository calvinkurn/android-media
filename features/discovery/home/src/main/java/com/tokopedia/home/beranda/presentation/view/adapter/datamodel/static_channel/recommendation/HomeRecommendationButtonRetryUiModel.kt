package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

class HomeRecommendationButtonRetryUiModel(
    val id: String = ID
) : BaseHomeRecommendationVisitable {
    override fun areItemsTheSame(other: Any): Boolean {
        return other is HomeRecommendationButtonRetryUiModel && id == other.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        // always bind, don't have the specific field
        return true
    }

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    companion object {
        private const val ID = "HOME_RECOMMENDATION_BUTTON_RETRY"
    }
}
