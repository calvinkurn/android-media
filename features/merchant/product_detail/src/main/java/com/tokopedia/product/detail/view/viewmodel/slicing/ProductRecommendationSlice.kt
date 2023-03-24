package com.tokopedia.product.detail.view.viewmodel.slicing

import androidx.lifecycle.LiveData
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

interface ProductRecommendationSlice {

    val loadViewToView: LiveData<Result<RecommendationWidget>>

    val verticalRecommendation: LiveData<Result<RecommendationWidget>>

    fun loadViewToView(pageName: String, productId: String, isTokoNow: Boolean)

    fun getVerticalRecommendationData(
        pageName: String,
        page: Int? = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
        productId: String?
    )
}
