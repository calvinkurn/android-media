package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class HomeRecommendationBannerTopAdsUiModel(
    val topAdsImageViewModel: TopAdsImageViewModel? = null,
    val cardId: String,
    val layoutCard: String,
    val layoutItem: String,
    val categoryId: String,
    val position: Int = -1,
    val pageName: String
) : BaseHomeRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is HomeRecommendationBannerTopAdsUiModel &&
            topAdsImageViewModel?.bannerId == other.topAdsImageViewModel?.bannerId
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return other == this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeRecommendationBannerTopAdsUiModel

        if (topAdsImageViewModel != other.topAdsImageViewModel) return false

        return true
    }
}
