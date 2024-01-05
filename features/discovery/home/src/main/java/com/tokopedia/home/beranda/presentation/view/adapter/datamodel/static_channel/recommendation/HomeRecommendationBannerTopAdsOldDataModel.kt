package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class HomeRecommendationBannerTopAdsOldDataModel(
    val topAdsImageViewModel: TopAdsImageViewModel? = null,
    val position: Int = -1,
    val bannerType: String
) : BaseHomeRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is HomeRecommendationBannerTopAdsOldDataModel &&
            topAdsImageViewModel?.bannerId == other.topAdsImageViewModel?.bannerId
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeRecommendationBannerTopAdsOldDataModel

        if (topAdsImageViewModel != other.topAdsImageViewModel) return false

        return true
    }
}
