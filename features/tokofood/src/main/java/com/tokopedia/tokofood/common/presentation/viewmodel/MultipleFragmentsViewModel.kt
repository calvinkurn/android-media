package com.tokopedia.tokofood.common.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.usecase.AddToCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.LoadCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.RemoveCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateCartTokoFoodUseCase
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.NonNullLiveData
import com.tokopedia.tokofood.common.util.Result
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
class MultipleFragmentsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loadCartTokoFoodUseCase: Lazy<LoadCartTokoFoodUseCase>,
    private val addToCartTokoFoodUseCase: Lazy<AddToCartTokoFoodUseCase>,
    private val updateCartTokoFoodUseCase: Lazy<UpdateCartTokoFoodUseCase>,
    private val removeCartTokoFoodUseCase: Lazy<RemoveCartTokoFoodUseCase>
) : ViewModel(), CoroutineScope {

    private val cartDataState = MutableStateFlow<CheckoutTokoFoodData?>(null)
    val cartDataFlow = cartDataState.asStateFlow()

    private val cartDataValidationState = MutableSharedFlow<UiEvent>()
    val cartDataValidationFlow: SharedFlow<UiEvent>
        get() = cartDataValidationState

    private val miniCartUiModelState =
        MutableStateFlow<Result<MiniCartUiModel>>(Result.Success(MiniCartUiModel()))
    val miniCartFlow = miniCartUiModelState.asStateFlow()

    private val miniCartLoadingQueue = NonNullLiveData(-Int.ONE)

    val shopId: String
        get() = cartDataState.value?.shop?.shopId.orEmpty()

    companion object {
        const val MINI_CART_STATE_KEY = "mini_cart_state_key"
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    fun onSavedInstanceState() {
        savedStateHandle[MINI_CART_STATE_KEY] =
            (miniCartUiModelState.replayCache.firstOrNull() as? Result.Success<MiniCartUiModel>)?.data
    }

    fun onRestoreSavedInstanceState() {
        miniCartUiModelState.tryEmit(
            Result.Success(
                savedStateHandle.get(MINI_CART_STATE_KEY) as? MiniCartUiModel ?: MiniCartUiModel()
            )
        )
    }

    fun loadInitial(source: String) {
        cartDataState.value.let { cartData ->
            if (cartData == null) {
                loadCartList(source)
            } else {
                launch {
                    setMiniCartValue(cartData)
                }
            }
        }
    }

    fun loadCartList(response: CheckoutTokoFood) {
        cartDataState.value = response.data
    }

    fun deleteProduct(
        productId: String,
        cartId: String,
        source: String,
        shopId: String? = null,
        shouldRefreshCart: Boolean = true
    ) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            val paramShopId = shopId ?: this@MultipleFragmentsViewModel.shopId
            val removeCartParam =
                RemoveCartTokoFoodParam.getProductParamById(productId, cartId, paramShopId)
            removeCartTokoFoodUseCase.get()(removeCartParam).collect {
                if (shouldRefreshCart) {
                    loadCartList(source)
                }
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_DELETE_PRODUCT,
                        data = cartId to it.data
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_DELETE_PRODUCT,
                    throwable = it
                )
            )
        })
    }

    fun deleteAllAtcAndAddProduct(updateParam: UpdateParam, source: String) {
        launchCatchError(block = {
            val removeCartParam = getRemoveAllProductParamByIdList()
            if (removeCartParam.carts.isNotEmpty()) {
                cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
                removeCartTokoFoodUseCase.get()(removeCartParam).collect {
                    loadCartList(source)
                    addToCart(updateParam, source)
                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_DELETE_PRODUCT,
                    throwable = it
                )
            )
        })
    }

    fun deleteUnavailableProducts() {
        launchCatchError(block = {
            val paramShopId = shopId
            val removeCartParam = getUnavailableProductsParam(paramShopId)
            if (removeCartParam.carts.isNotEmpty()) {
                cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
                removeCartTokoFoodUseCase.get()(removeCartParam).collect {
                    cartDataValidationState.emit(
                        UiEvent(state = UiEvent.EVENT_SUCCESS_DELETE_UNAVAILABLE_PRODUCTS)
                    )
                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_DELETE_PRODUCT,
                    throwable = it
                )
            )
        })
    }

    fun updateNotes(
        updateParam: UpdateParam,
        source: String,
        shouldRefreshCart: Boolean = true
    ) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            updateCartTokoFoodUseCase.get()(updateParam).collect {
                if (shouldRefreshCart) {
                    loadCartList(source)
                }
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_NOTES,
                        data = updateParam to it.data
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_UPDATE_NOTES,
                    throwable = it
                )
            )
        })
    }

    fun updateQuantity(
        updateParam: UpdateParam,
        source: String,
        shouldRefreshCart: Boolean = true
    ) {
        launchCatchError(block = {
            updateCartTokoFoodUseCase.get()(updateParam).collect {
                if (shouldRefreshCart) {
                    loadCartList(source)
                }
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY,
                        data = updateParam to it.data
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_UPDATE_QUANTITY,
                    throwable = it
                )
            )
        })
    }

    fun updateCart(updateParam: UpdateParam, source: String) {
        launchCatchError(block = {
            updateCartTokoFoodUseCase.get()(updateParam).collect {
                loadCartList(source)
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_CART,
                        data = updateParam to it.data
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_UPDATE_CART,
                    throwable = it
                )
            )
        })
    }

    fun addToCart(updateParam: UpdateParam, source: String) {
        launchCatchError(block = {
            addToCartTokoFoodUseCase.get()(updateParam).collect {
                if (it.data.bottomSheet.isShowBottomSheet) {
                    cartDataValidationState.emit(
                        UiEvent(
                            state = UiEvent.EVENT_PHONE_VERIFICATION,
                            data = it.data.bottomSheet
                        )
                    )
                } else {
                    loadCartList(source)
                    cartDataValidationState.emit(
                        UiEvent(
                            state = UiEvent.EVENT_SUCCESS_ADD_TO_CART,
                            data = updateParam to it.data
                        )
                    )

                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_ADD_TO_CART,
                    throwable = it
                )
            )
        })
    }

    fun clickMiniCart() {
        launch {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT,
                    data = cartDataFlow.value
                )
            )
        }
    }

    private fun loadCartList(source: String) {
        launchCatchError(block = {
            miniCartLoadingQueue.value = miniCartLoadingQueue.value.plus(Int.ONE)
            miniCartUiModelState.emit(Result.Loading())
            loadCartTokoFoodUseCase.get()(source).collect {
                cartDataState.emit(it.data)
                setMiniCartValue(it.data)
            }
        }, onError = {
            miniCartLoadingQueue.value.minus(Int.ONE)
            miniCartUiModelState.emit(Result.Failure(it))
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_LOAD_CART,
                    throwable = it
                )
            )
        })
    }

    private suspend fun setMiniCartValue(data: CheckoutTokoFoodData) {
        miniCartLoadingQueue.value = miniCartLoadingQueue.value.minus(Int.ONE)
        if (miniCartLoadingQueue.value.isLessThanZero()) {
            miniCartUiModelState.emit(Result.Success(data.getMiniCartUiModel()))
        }
        val shouldShowMiniCart =
            data.shop.shopId.isNotBlank() && data.getProductListFromCart().isNotEmpty()
        val state =
            if (shouldShowMiniCart) {
                UiEvent.EVENT_SUCCESS_LOAD_CART
            } else {
                UiEvent.EVENT_FAILED_LOAD_CART
            }
        cartDataValidationState.emit(UiEvent(state = state))
    }

    private fun getRemoveAllProductParamByIdList(): RemoveCartTokoFoodParam {
        return cartDataState.value?.getRemoveAllCartParam(shopId) ?: RemoveCartTokoFoodParam()
    }

    private fun getUnavailableProductsParam(shopId: String): RemoveCartTokoFoodParam {
        return cartDataState.value?.getRemoveUnavailableCartParam(shopId)
            ?: RemoveCartTokoFoodParam()
    }

}
