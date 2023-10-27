package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class HomeRecommendationHeadlineTopAdsDataModel(
    val headlineAds: CpmModel = CpmModel(),
    val position: Int = -1
) : HomeRecommendationVisitable, ImpressHolder() {

    override fun equalsDataModel(dataModel: Visitable<HomeRecommendationTypeFactory>): Boolean {
        return dataModel == this
    }

    override fun getUniqueIdentity(): Any {
        return headlineAds.data.firstOrNull()?.id ?: ""
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
