package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import android.os.Bundle
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

class HomeRecommendationEmpty : HomeRecommendationVisitable {
    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        return dataModel == this
    }

    override fun getUniqueIdentity(): Any {
        return ID
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object{
        private const val ID = "RECOMMENDATION_EMPTY"
    }

}