package com.tokopedia.home_recom.model.dataModel

import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.R

class RecommendationScrollDataModel(
        val products: List<ProductDataModel>
) : BaseHomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_product_info
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

}