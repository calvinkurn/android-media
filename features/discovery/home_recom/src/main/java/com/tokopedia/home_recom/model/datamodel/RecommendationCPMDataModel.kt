package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse

/**
 * Created by yfsx on 18/08/21.
 */
data class RecommendationCPMDataModel(
        val topAdsHeadlineResponse: TopAdsHeadlineResponse = TopAdsHeadlineResponse(),
        val parentPosition: Int = 0
) : HomeRecommendationDataModel {
    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = topAdsHeadlineResponse.toString()

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean {
        return if (newData is RecommendationCPMDataModel) {
            topAdsHeadlineResponse == newData.topAdsHeadlineResponse &&
                    parentPosition == newData.parentPosition
        } else {
            false
        }
    }

    override fun newInstance(): HomeRecommendationDataModel = this.copy()

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null
}