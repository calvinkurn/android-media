package com.tokopedia.product.detail.view.viewmodel.product_detail

import androidx.lifecycle.LiveData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Result

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

interface IProductRecommSubViewModel {

    val loadViewToView: LiveData<Result<RecommendationWidget>>

    val verticalRecommendation: LiveData<Result<RecommendationWidget>>

    val statusFilterTopAdsProduct: LiveData<Result<Boolean>>

    val filterTopAdsProduct: LiveData<ProductRecommendationDataModel>

    val loadTopAdsProduct: LiveData<Result<RecommendationWidget>>

    fun loadViewToView(pageName: String, productId: String, isTokoNow: Boolean)

    fun getVerticalRecommendationData(
        pageName: String,
        page: Int? = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
        productId: String?
    )

    fun recommendationChipClicked(
        recommendationDataModel: ProductRecommendationDataModel,
        annotationChip: AnnotationChip,
        productId: String
    )

    fun loadRecommendation(
        pageName: String,
        productId: String,
        isTokoNow: Boolean,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?
    )

    fun onResetAlreadyRecomHit()
}
