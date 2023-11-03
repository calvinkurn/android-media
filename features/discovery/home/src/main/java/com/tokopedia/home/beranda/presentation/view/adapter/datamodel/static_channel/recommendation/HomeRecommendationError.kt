package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl

class HomeRecommendationError(val throwable: Throwable? = null) : HomeRecommendationVisitable {

    override fun equalsDataModel(dataModel: Visitable<HomeRecommendationTypeFactoryImpl>): Boolean {
        return dataModel == this
    }

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun getUniqueIdentity(): Any {
        return ID
    }

    companion object {
        private const val ID = "RECOMMENDATION_ERROR"
    }
}
