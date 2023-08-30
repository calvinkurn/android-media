package com.tokopedia.recommendation_widget_common.infinite.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.infinite.component.product.InfiniteProductUiModel
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfiniteRecommendationViewModel @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase,
) : ViewModel() {

    companion object {
        private const val DEFAULT_CURRENT_PAGE = 0
        private const val DEFAULT_NEXT_PAGE = 1
    }

    private val _recommendationProducts = MutableLiveData<List<InfiniteRecommendationUiModel>>()
    val recommendationProducts: LiveData<List<InfiniteRecommendationUiModel>>
        get() = _recommendationProducts

    private var currentPage = DEFAULT_CURRENT_PAGE
    private var nextPage = DEFAULT_NEXT_PAGE

    fun init() {
        currentPage = DEFAULT_CURRENT_PAGE
        nextPage = DEFAULT_NEXT_PAGE
    }

    fun getVerticalRecommendationData(
        productId: String,
        pageName: String,
    ) {
        if (currentPage == nextPage) return
        currentPage.inc()

        viewModelScope.launch {
            runCatching {
                val requestParams = GetRecommendationRequestParam(
                    pageNumber = nextPage,
                    pageName = pageName,
                    productIds = arrayListOf(productId)
                )
                val recommendationResponse = getRecommendationUseCase.getData(requestParams)
                val dataResponse = recommendationResponse.firstOrNull()
                if (dataResponse == null) {
                    _recommendationProducts.value = emptyList()
                } else {
                    val products = dataResponse.recommendationItemList.map {
                        InfiniteProductUiModel(it)
                    }

                    if (dataResponse.hasNext) {
                        nextPage = dataResponse.nextPage
                    }

                    _recommendationProducts.value = products
                }
            }.onFailure {
                _recommendationProducts.value = emptyList()
            }
        }
    }
}
