package com.tokopedia.product.detail.view.viewmodel.product_detail

import androidx.lifecycle.LiveData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.event.ProductRecommendationEvent
import com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel.ProductRecommUiState
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by yovi.putra on 24/03/23"
 * Project name: android-tokopedia-core
 **/

interface IProductRecommSubViewModel {

    val statusFilterTopAdsProduct: LiveData<Result<Boolean>>

    val filterTopAdsProduct: LiveData<ProductRecommendationDataModel>

    val loadTopAdsProduct: LiveData<Result<RecommendationWidget>>

    val productListData : StateFlow<MutableList<ProductRecommUiState>>
    fun onRecommendationEvent(event: ProductRecommendationEvent)

    fun recommendationChipClicked(
        recommendationDataModel: ProductRecommendationDataModel,
        annotationChip: AnnotationChip,
        productId: String
    )

    fun loadRecommendation(
        pageName: String,
        productId: String,
        isTokoNow: Boolean,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?,
        queryParam: String,
        thematicId: String
    )

    fun onResetAlreadyRecomHit()
}
