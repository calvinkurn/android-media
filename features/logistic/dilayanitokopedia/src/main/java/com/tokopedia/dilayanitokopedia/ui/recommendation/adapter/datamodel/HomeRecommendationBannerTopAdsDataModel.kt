package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel

import android.os.Bundle
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.factory.HomeRecommendationTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.smart_recycler_helper.SmartVisitable
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

data class HomeRecommendationBannerTopAdsDataModel(
        val topAdsImageUiModel: TopAdsImageUiModel? = null,
        val position: Int = -1
) : HomeRecommendationVisitable, ImpressHolder() {
    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        return dataModel == this
    }

    override fun getUniqueIdentity(): Any {
        return topAdsImageUiModel?.bannerId ?: ""
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
