package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class HomeRecommendationHeadlineTopAdsDataModel(
    val headlineAds: CpmModel = CpmModel(),
    val position: Int = -1
) : BaseHomeRecommendationVisitable, ImpressHolder() {
    override fun areItemsTheSame(other: Any): Boolean {
        return other is HomeRecommendationHeadlineTopAdsDataModel &&
            other.headlineAds.data.firstOrNull()?.id == headlineAds.data.firstOrNull()?.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
