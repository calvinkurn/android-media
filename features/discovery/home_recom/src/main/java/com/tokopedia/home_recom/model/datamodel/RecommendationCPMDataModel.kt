package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse

/**
 * Created by yfsx on 18/08/21.
 */
class RecommendationCPMDataModel(
        val topAdsHeadlineResponse: TopAdsHeadlineResponse = TopAdsHeadlineResponse(),
        val parentPosition: Int = 0
) : HomeRecommendationDataModel {
    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}