package com.tokopedia.dilayanitokopedia.ui.home.presentation.uimodel

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.ui.home.presentation.factory.HomeRecommendationTypeFactory
import com.tokopedia.smart_recycler_helper.SmartVisitable

interface HomeRecommendationVisitable : SmartVisitable<HomeRecommendationTypeFactory> {
    fun getChangePayloadFrom(b: Any?): Bundle?
}
