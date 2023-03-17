package com.tokopedia.dilayanitokopedia.ui.home.presentation.datamodel.recommendationforyou

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.ui.home.presentation.factory.HomeRecommendationTypeFactory
import com.tokopedia.dilayanitokopedia.ui.home.presentation.uimodel.HomeRecommendationVisitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.smart_recycler_helper.SmartVisitable
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class HomeRecommendationHeadlineTopAdsDataModel(
    val headlineAds: CpmModel = CpmModel(),
    val position: Int = -1
) : HomeRecommendationVisitable, ImpressHolder() {
    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        return dataModel == this
    }

    override fun getUniqueIdentity(): Any {
        return headlineAds.data?.firstOrNull()?.id ?: ""
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
