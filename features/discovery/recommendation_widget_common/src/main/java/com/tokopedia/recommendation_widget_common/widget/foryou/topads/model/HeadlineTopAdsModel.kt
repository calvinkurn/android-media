package com.tokopedia.recommendation_widget_common.widget.foryou.topads.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationVisitable
import com.tokopedia.topads.sdk.domain.model.CpmModel

data class HeadlineTopAdsModel(
    val headlineAds: CpmModel = CpmModel(),
    val position: Int = -1
) : ForYouRecommendationVisitable, ImpressHolder() {

    override fun areItemsTheSame(other: Any): Boolean {
        return other is HeadlineTopAdsModel &&
            other.headlineAds.data.firstOrNull()?.id == headlineAds.data.firstOrNull()?.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}

