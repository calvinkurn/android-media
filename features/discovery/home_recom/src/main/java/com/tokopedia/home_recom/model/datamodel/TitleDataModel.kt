package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory

class TitleDataModel (
        val title: String
) : HomeRecommendationDataModel {

    companion object {
        val LAYOUT = R.layout.fragment_title
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)
}
