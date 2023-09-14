package com.tokopedia.recommendation_widget_common.widget.global

import androidx.lifecycle.viewModelScope
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.mvvm.ViewModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.carousel.global.tracking.RecommendationCarouselWidgetTrackingATC
import com.tokopedia.recommendation_widget_common.widget.cart.CartService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecommendationWidgetViewModel @Inject constructor(
    state: RecommendationWidgetState = RecommendationWidgetState(),
    private val getRecommendationWidgetUseCase: GetRecommendationUseCase,
    private val cartService: dagger.Lazy<CartService>,
): androidx.lifecycle.ViewModel(),
    ViewModel<RecommendationWidgetState> {

    private val _stateFlow = MutableStateFlow(state)
    override val stateFlow: StateFlow<RecommendationWidgetState>
        get() = _stateFlow

    private val stateValue: RecommendationWidgetState
        get() = stateFlow.value

    private fun updateState(action: (RecommendationWidgetState) -> RecommendationWidgetState) {
        _stateFlow.update(action)
    }

    internal fun bind(model: RecommendationWidgetModel) {
        if (model.widget != null) {
            updateState { it.from(model, listOf(model.widget)) }
        } else if (!stateValue.contains(model)) {
            updateState { it.loading(model) }

            viewModelScope.launch {
                try {
                    tryGetRecommendationWidget(model)
                } catch (ignored: Throwable) {
                    onGetRecommendationWidgetError(model)
                }
            }
        }
    }

    private suspend fun tryGetRecommendationWidget(model: RecommendationWidgetModel) {
        val recommendationWidgetList = getRecommendationWidgetUseCase.getData(
            GetRecommendationRequestParam(
                pageNumber = model.metadata.pageNumber,
                productIds = model.metadata.productIds,
                queryParam = model.metadata.queryParam,
                pageName = model.metadata.pageName,
                categoryIds = model.metadata.categoryIds,
                keywords = model.metadata.keyword,
                isTokonow = model.metadata.isTokonow,
            )
        )

        updateState { it.from(model, recommendationWidgetList) }

        getMiniCart()
    }

    private fun getMiniCart() {
        val shopId = stateValue.miniCartShopId
        val miniCartSource = stateValue.miniCartSource

        if (shopId.isNotEmpty() && miniCartSource != null)
            cartService.get().getMiniCart(shopId, miniCartSource) { miniCartData ->
                updateState { it.refreshMiniCart(miniCartData) }
            }
    }

    private fun onGetRecommendationWidgetError(model: RecommendationWidgetModel) {
        updateState { it.error(model) }
    }

    fun refresh() {
        updateState { it.clear() }
    }

    internal fun onAddToCartNonVariant(
        model: RecommendationCarouselModel,
        item: RecommendationItem,
        updatedQuantity: Int,
    ) {
        val miniCartSource = stateValue.miniCartSource ?: return
        val productId = item.productId.toString()

        cartService.get().handleCart(
            productId = productId,
            shopId = item.shopId.toString(),
            currentQuantity = item.quantity,
            updatedQuantity = updatedQuantity,
            miniCartItem = stateValue.getMiniCartItemProduct(productId),
            miniCartSource = miniCartSource,
            onSuccessAddToCart = { addToCartData, miniCartData ->
                val atcTrackingData = RecommendationCarouselWidgetTrackingATC(
                    item,
                    addToCartData.data.cartId,
                    addToCartData.data.quantity,
                )
                model.widgetTracking?.sendEventAddToCart(atcTrackingData)

                updateState {
                    it.refreshMiniCart(
                        miniCartData = miniCartData,
                        successMessage = addToCartData.errorMessage.joinToString(separator = ", "),
                    )
                }
            },
            onSuccessUpdateCart = { updateCartData, miniCartData ->
                model.widgetTracking?.sendEventUpdateCart()

                updateState {
                    it.refreshMiniCart(
                        miniCartData = miniCartData,
                        successMessage = updateCartData.error.joinToString(separator = ", "),
                    )
                }
            },
            onSuccessDeleteCart = { deleteCartData, miniCartData ->
                model.widgetTracking?.sendEventDeleteCart()

                updateState {
                    it.refreshMiniCart(
                        miniCartData = miniCartData,
                        successMessage = deleteCartData.errorMessage.joinToString(separator = ", "),
                    )
                }
            },
            onError = { throwable ->
                updateState { it.showErrorMessage(throwable.message ?: "") }
            }
        )
    }

    fun dismissMessage() {
        updateState { it.dismissMessage() }
    }
}
