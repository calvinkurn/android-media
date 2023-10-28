package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

class HomeRecommendationLoading : HomeRecommendationVisitable {
    override fun getUniqueIdentity(): Any {
        return LOADING_ID
    }

    override fun equalsDataModel(dataModel: Visitable<HomeRecommendationTypeFactoryImpl>): Boolean {
        return dataModel == this
    }

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    companion object {
        private const val LOADING_ID = "RECOMMENDATION_LOADING"
    }
}
