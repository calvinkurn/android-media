package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory

/**
 * Created by Lukas on 08/10/20.
 */

data class RecommendationErrorDataModel(
        val throwable: Throwable
): HomeRecommendationDataModel {
    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object{
        val LAYOUT = R.layout.item_recommendation_error
    }
}
interface RecommendationErrorListener{
    fun onRefreshRecommendation()
    fun onCloseRecommendation()
}