package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ShoppingListAnotherOptionBottomSheetVisitableMapper.addEmptyState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ShoppingListAnotherOptionBottomSheetVisitableMapper.addErrorState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ShoppingListAnotherOptionBottomSheetVisitableMapper.addRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ShoppingListAnotherOptionBottomSheetVisitableMapper.addShimmeringRecommendedProducts
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ShoppingListAnotherOptionBottomSheetViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val productRecommendationUseCase: GetSingleRecommendationUseCase,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io)  {
    private companion object {
        const val RECOMMENDATION_PAGE_NAME = "tokonow_similar"
    }

    private val layout: MutableList<Visitable<*>> = mutableListOf()

    private val _productRecommendation: MutableStateFlow<UiState<List<Visitable<*>>>> = MutableStateFlow(
        UiState.Loading(
            data = layout.addShimmeringRecommendedProducts()
        )
    )
    val productRecommendation: StateFlow<UiState<List<Visitable<*>>>>
        get() = _productRecommendation

    fun loadLayout(productId: String) {
        launchCatchError(
            block = {
                val param = GetRecommendationRequestParam(
                    userId = userSession.userId.toIntSafely(),
                    productIds = listOf(productId),
                    pageName = RECOMMENDATION_PAGE_NAME,
                    xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                    xSource = X_SOURCE_RECOMMENDATION_PARAM,
                    isTokonow = true
                )
                val productRecommendation = productRecommendationUseCase.getData(param)
                layout.clear()
                _productRecommendation.value = UiState.Success(
                    data = if (productRecommendation.recommendationItemList.isNotEmpty())
                        layout.addRecommendedProducts(productRecommendation)
                    else
                        layout.addEmptyState()
                )
            },
            onError = { throwable ->
                layout.clear()
                _productRecommendation.value = UiState.Error(
                    data = layout.addErrorState(throwable),
                    throwable = throwable
                )
            }
        )
    }

    fun loadLoadingState() {
        layout.clear()
        _productRecommendation.value = UiState.Loading(
            data = layout.addShimmeringRecommendedProducts()
        )
    }
}
