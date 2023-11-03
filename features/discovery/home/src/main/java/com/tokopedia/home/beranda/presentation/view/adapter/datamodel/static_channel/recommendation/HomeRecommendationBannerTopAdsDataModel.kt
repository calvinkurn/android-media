package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_BANNER_ADS
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class HomeRecommendationBannerTopAdsDataModel(
    val topAdsImageViewModel: TopAdsImageViewModel? = null,
    val position: Int = -1,
    val bannerType: String = TYPE_BANNER_ADS
) : Visitable<HomeRecommendationTypeFactoryImpl>, ImpressHolder() {

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
