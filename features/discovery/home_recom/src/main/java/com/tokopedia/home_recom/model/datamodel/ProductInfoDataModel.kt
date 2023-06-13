package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.entity.ProductDetailData
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class ProductInfoDataModel(
        val productDetailData: ProductDetailData? = null,
        var isGetTopAds: Boolean = false
) : HomeRecommendationDataModel, ImpressHolder() {

    companion object {
        val LAYOUT = R.layout.fragment_product_info
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

    override fun name(): String = productDetailData?.name ?: ""

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean {
        return if (newData is ProductInfoDataModel) {
            productDetailData.hashCode() == newData.productDetailData.hashCode()
        } else {
            false
        }
    }

    override fun newInstance(): HomeRecommendationDataModel {
        return this.copy()
    }

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null


}