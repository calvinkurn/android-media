package com.tokopedia.home_recom.model.datamodel

import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory

class ProductInfoDataModel(
        val productDetailData: ProductDetailData
) : BaseHomeRecommendationDataModel {

    companion object{
        val LAYOUT = R.layout.fragment_product_info
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

}