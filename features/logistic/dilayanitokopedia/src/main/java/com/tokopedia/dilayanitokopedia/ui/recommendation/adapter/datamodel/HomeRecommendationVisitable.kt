package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.factory.HomeRecommendationTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

interface HomeRecommendationVisitable : SmartVisitable<HomeRecommendationTypeFactory> {
    fun getChangePayloadFrom(b: Any?): Bundle?
}
