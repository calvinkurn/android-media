package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.sortfilter.SortFilterItem

/**
 * Created by Lukas on 08/10/20.
 */

data class RecommendationEmptyDataModel(
        val selectedFilter: List<SortFilterItem>
): HomeRecommendationDataModel {
    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object{
        val LAYOUT = R.layout.item_recommendation_empty
    }
}