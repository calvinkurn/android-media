package com.tokopedia.recommendation_widget_common.infinite.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.infinite.component.loading.InfiniteLoadingUiModel
import com.tokopedia.recommendation_widget_common.infinite.component.product.InfiniteProductUiModel
import com.tokopedia.recommendation_widget_common.infinite.component.title.InfiniteTitleUiModel
import com.tokopedia.recommendation_widget_common.infinite.main.base.InfiniteRecommendationUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class InfiniteRecommendationViewModel @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase
) : ViewModel() {

    companion object {
        private const val DEFAULT_CURRENT_PAGE = 0
        private const val DEFAULT_NEXT_PAGE = 1
    }

    private val _components = MutableLiveData<List<InfiniteRecommendationUiModel>>()
    val components: LiveData<List<InfiniteRecommendationUiModel>> = _components

    private var currentPage = DEFAULT_CURRENT_PAGE
    private var nextPage = DEFAULT_NEXT_PAGE

    fun init() {
        currentPage = DEFAULT_CURRENT_PAGE
        nextPage = DEFAULT_NEXT_PAGE
        _components.value = listOf(InfiniteLoadingUiModel)
    }

    fun fetchComponents(
        productId: String,
        pageName: String
    ) {
        if (currentPage == nextPage) return
        currentPage = nextPage

        viewModelScope.launch(Dispatchers.IO) {
            val requestParams = GetRecommendationRequestParam(
                pageNumber = nextPage,
                pageName = pageName,
                productIds = listOf(productId)
            )
            val recommendationResponse = getRecommendationUseCase.getData(requestParams)
            val recommendationWidget = recommendationResponse.firstOrNull()

            val components = recommendationWidget.toComponents()
            _components.postValue(components)
        }
    }

    private fun RecommendationWidget?.toComponents(): List<InfiniteRecommendationUiModel> {
        val components = _components.value?.toMutableList() ?: mutableListOf()

        if (this == null) {
            components.removeLast()
        } else {
            /**
             * Add title only for the first page
             */
            if (currentPage == DEFAULT_NEXT_PAGE) {
                components.add(0, InfiniteTitleUiModel(title))
            }

            val products = recommendationItemList.map { InfiniteProductUiModel(it) }
            components.addAll(components.size - 1, products)

            if (!hasNext) components.removeLast()

            this@InfiniteRecommendationViewModel.nextPage = nextPage
        }

        return components
    }
}
