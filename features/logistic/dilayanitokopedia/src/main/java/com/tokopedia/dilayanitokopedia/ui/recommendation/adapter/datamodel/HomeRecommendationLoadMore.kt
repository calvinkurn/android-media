package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.factory.HomeRecommendationTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

class HomeRecommendationLoadMore : HomeRecommendationVisitable {
    override fun getUniqueIdentity(): Any {
        return LOADING_ID
    }

    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        return dataModel == this
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        private const val LOADING_ID = "RECOMMENDATION_LOADING_MORE"
    }
}
