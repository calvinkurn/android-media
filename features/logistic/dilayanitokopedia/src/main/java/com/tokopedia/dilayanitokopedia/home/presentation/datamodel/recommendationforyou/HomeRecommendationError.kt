package com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.home.presentation.factory.HomeRecommendationTypeFactory
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.HomeRecommendationVisitable
import com.tokopedia.smart_recycler_helper.SmartVisitable

class HomeRecommendationError : HomeRecommendationVisitable {
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

    companion object {
        private const val ID = "RECOMMENDATION_ERROR"
    }
}
