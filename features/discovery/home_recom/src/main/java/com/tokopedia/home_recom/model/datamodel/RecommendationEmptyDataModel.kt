package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory

/**
 * Created by Lukas on 08/10/20.
 */

data class RecommendationEmptyDataModel(
        val name: String = "recomLoading",
        val type: Int = TYPE_DEFAULT
) : HomeRecommendationDataModel {
    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        val LAYOUT = R.layout.item_recommendation_empty
        val TYPE_DEFAULT = 0
        val TYPE_PAGE_INFINITE_RECOM = 1
    }

    override fun name(): String = name

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean = newData is RecommendationEmptyDataModel

    override fun newInstance(): HomeRecommendationDataModel = this.copy()

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null
}