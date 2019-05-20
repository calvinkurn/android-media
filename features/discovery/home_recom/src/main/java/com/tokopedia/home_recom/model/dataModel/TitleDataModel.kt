package com.tokopedia.home_recom.model.dataModel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.homerecommendation.HomeRecommendationTypeFactory

class TitleDataModel (
        val title: String
) : BaseHomeRecommendationDataModel {

    companion object {
        val LAYOUT = R.layout.fragment_title
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)
}
