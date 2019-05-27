package com.tokopedia.home_recom.model.dataModel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.entity.Data
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.view.adapter.homerecommendation.HomeRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class ProductInfoDataModel(
        val productDetailData: ProductDetailData
) : BaseHomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_product_info
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

}