package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.viewholder.TitleViewHolder

/**
 * A Class of DataModel.
 *
 * This class for holding data for type factory pattern [TitleViewHolder]
 */
class TitleDataModel (
        val title: String,
        val pageName: String,
        val seeMoreAppLink: String
) : HomeRecommendationDataModel {

    companion object {
        val LAYOUT = R.layout.fragment_title
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)
}

interface TitleListener{
    fun onClickSeeMore(pageName: String, seeMoreAppLink: String)
}