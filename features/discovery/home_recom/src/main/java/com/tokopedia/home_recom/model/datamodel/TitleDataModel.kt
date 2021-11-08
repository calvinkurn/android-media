package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.viewholder.TitleViewHolder

/**
 * A Class of DataModel.
 *
 * This class for holding data for type factory pattern [TitleViewHolder]
 */
data class TitleDataModel(
        val title: String,
        val pageName: String,
        val seeMoreAppLink: String
) : HomeRecommendationDataModel {

    companion object {
        val LAYOUT = R.layout.fragment_title
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)


    override fun name(): String = pageName

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean {
        return if (newData is TitleDataModel) {
            title == newData.title
                    && pageName == newData.pageName
                    && seeMoreAppLink == newData.seeMoreAppLink
        } else {
            false
        }
    }

    override fun newInstance(): HomeRecommendationDataModel = this.copy()

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null
}

interface TitleListener{
    fun onClickSeeMore(pageName: String, seeMoreAppLink: String)
}