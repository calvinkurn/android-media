package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by yfsx on 16/09/21.
 */
data class FirstLoadDataModel(
        val id: String = "",
        val name: String = ""
) : HomeRecommendationDataModel, ImpressHolder() {

    override fun name(): String = name

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean = newData is RecommendationEmptyDataModel

    override fun newInstance(): HomeRecommendationDataModel = this.copy()

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)
}