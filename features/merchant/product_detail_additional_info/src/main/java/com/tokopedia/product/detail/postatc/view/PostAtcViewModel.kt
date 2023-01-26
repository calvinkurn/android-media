package com.tokopedia.product.detail.postatc.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.product.detail.postatc.usecase.GetPostAtcLayoutUseCase
import com.tokopedia.product.detail.postatc.util.mapToUiModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import javax.inject.Inject

class PostAtcViewModel @Inject constructor(
    private val getPostAtcLayoutUseCase: GetPostAtcLayoutUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _layouts = MutableLiveData<List<PostAtcUiModel>>()
    val layouts: LiveData<List<PostAtcUiModel>> = _layouts

    private val _recommendations = MutableLiveData<List<RecommendationWidget>>()
    val recommendations: LiveData<List<RecommendationWidget>> = _recommendations

    fun fetchLayout(
        productId: String,
        cartId: String,
        layoutId: String
    ) {
        launchCatchError(block = {
            val result = getPostAtcLayoutUseCase.execute(
                productId,
                cartId,
                layoutId
            )

            val uiModels = result.components.mapToUiModel()
            _layouts.postValue(uiModels)

            fetchRecommendation(productId)

        }, onError = {
            it
        })
    }

    fun fetchRecommendation(productId: String) {
        launchCatchError(block = {
            val requestParams = GetRecommendationRequestParam(
                pageNumber = 1,
                pageName = "pdp_atc_1,pdp_atc_2",
                productIds = listOf(productId)
            )
            val result = getRecommendationUseCase.getData(requestParams)
            _recommendations.postValue(result)
        }, onError = {
            it
        })

    }

}
