package com.tokopedia.tokofood.common.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFood
import com.tokopedia.tokofood.common.domain.param.CartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.domain.usecase.LoadCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.RemoveCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateCartTokoFoodUseCase
import com.tokopedia.tokofood.common.minicartwidget.domain.model.CartProduct
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.common.util.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
class MultipleFragmentsViewModel @Inject constructor(val savedStateHandle: SavedStateHandle,
                                                     private val loadCartTokoFoodUseCase: LoadCartTokoFoodUseCase,
                                                     private val updateCartTokoFoodUseCase: UpdateCartTokoFoodUseCase,
                                                     private val removeCartTokoFoodUseCase: RemoveCartTokoFoodUseCase
) : ViewModel(), CoroutineScope {
    val inputFlow = MutableSharedFlow<String>(1)

    val isDebug = true

    private val cartDataState = MutableStateFlow(CheckoutTokoFoodData())
    private val cartDataFlow = cartDataState.asStateFlow()

    private val cartDataValidationState = MutableSharedFlow<UiEvent>()
    val cartDataValidationFlow: SharedFlow<UiEvent>
        get() = cartDataValidationState

    private val miniCartUiModelState = MutableStateFlow<Result<MiniCartUiModel>>(Result.Success(MiniCartUiModel()))
    val miniCartFlow = miniCartUiModelState.asStateFlow()

    private val shopId: String
        get() = cartDataState.value.shop.shopId

    companion object {
        const val INPUT_KEY = "string_input"
        const val MINI_CART_STATE_KEY = "mini_cart_state_key"
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    fun onSavedInstanceState() {
        savedStateHandle[INPUT_KEY] = inputFlow.replayCache.firstOrNull()
        savedStateHandle[MINI_CART_STATE_KEY] = (miniCartUiModelState.replayCache.firstOrNull() as? Result.Success<MiniCartUiModel>)?.data
    }

    fun onRestoreSavednstanceState() {
        inputFlow.tryEmit(savedStateHandle[INPUT_KEY] ?: "")
        miniCartUiModelState.tryEmit(Result.Success(savedStateHandle[MINI_CART_STATE_KEY] ?: MiniCartUiModel()))
    }

    fun loadCartList() {
        launchCatchError(block = {
            // TODO: Check for whether the loading is already shown
            val loadCartParam = CheckoutTokoFoodParam()
            loadCartTokoFoodUseCase(loadCartParam).collect {
                cartDataState.emit(it.data)
                miniCartUiModelState.emit(Result.Success(mapCartDataToMiniCart(it.data)))
                cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_CART))
            }
        }, onError = {
            Timber.e(it)
        })
    }

    fun loadCartList(response: CheckoutTokoFoodResponse) {
        cartDataState.value = response.data
    }

    fun updateAndValidateCart() {
        // TODO: Check if this need to validate
    }

    fun deleteProduct(productId: String, cartId: String) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            val removeCartParam = getProductParamById(productId, cartId)
            removeCartTokoFoodUseCase(removeCartParam).collect {
                if (it.success == 1) {
                    loadCartList()
                    cartDataValidationState.emit(
                        UiEvent(
                            state = UiEvent.EVENT_SUCCESS_DELETE_PRODUCT,
                            data = cartId to it.data
                        )
                    )
                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_ERROR_VALIDATE,
                    throwable = it
                )
            )
        })
    }

    fun deleteUnavailableProducts() {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            val removeCartParam = getUnavailableProductsParam()
            removeCartTokoFoodUseCase(removeCartParam).collect {
                if (it.success == 1) {
                    loadCartList()
                    cartDataValidationState.emit(
                        UiEvent(state = UiEvent.EVENT_SUCCESS_DELETE_UNAVAILABLE_PRODUCTS)
                    )
                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_ERROR_VALIDATE,
                    throwable = it
                )
            )
        })
    }

    fun updateNotes(updateParam: UpdateParam) {
        launchCatchError(block = {
            val param = updateParam.apply {
                shopId = this@MultipleFragmentsViewModel.shopId
            }
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            updateCartTokoFoodUseCase(param).collect {
                if (it.success == 1) {
                    loadCartList()
                    cartDataValidationState.emit(
                        if (isDebug) {
                            updateParam.productList.firstOrNull().let { param ->
                                UiEvent(
                                    state = UiEvent.EVENT_SUCCESS_UPDATE_NOTES,
                                    data = updateParam to it.data.copy(carts = listOf(
                                        CartTokoFood(
                                            productId = param?.productId.orEmpty(),
                                            cartId = param?.cartId.orEmpty())
                                    ))
                                )
                            }
                        } else {
                            UiEvent(
                                state = UiEvent.EVENT_SUCCESS_UPDATE_NOTES,
                                data = updateParam to it.data
                            )
                        }
                    )
                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_ERROR_VALIDATE,
                    throwable = it
                )
            )
        })
    }

    fun updateCart(updateParam: UpdateParam) {
        launchCatchError(block = {
            updateCartTokoFoodUseCase(updateParam).collect {
                if (it.success == 1) {
                    loadCartList()
                    cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY))
                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_ERROR_VALIDATE,
                    throwable = it
                )
            )
        })
    }

    fun testUpdateCart() {
        launchCatchError(block = {
            miniCartUiModelState.value = Result.Loading()
            val testParam = UpdateParam()
            updateCartTokoFoodUseCase(testParam).collect {
                if (it.success == 1) {
                    loadCartList()
                    cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY))
                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_ERROR_VALIDATE,
                    throwable = it
                )
            )
        })

    }

    // TODO: Move to mapper
    private fun mapCartDataToMiniCart(cartData: CheckoutTokoFoodData): MiniCartUiModel {
        val products = cartData.availableSection.products
        return MiniCartUiModel(
            shopName = cartData.shop.name,
            totalPrice = cartData.shoppingSummary.total.cost,
            totalPriceFmt = cartData.shoppingSummary.total.costFmt,
            totalProductQuantity = products.size
        )
    }

    private fun calculateTotal(cartMapData: Map<String, CartProduct>): Pair<Int, Long> {
        var totalPrice = 0L
        var totalQuantity = 0
        cartMapData.forEach {
            totalPrice += it.value.price
            totalQuantity += it.value.quantity
        }

        return Pair(totalQuantity, totalPrice)
    }

    private fun getProductParamById(productId: String, cartId: String): CartTokoFoodParam {
        val cartList = listOf(
            CartItemTokoFoodParam(
                cartId = cartId.toLongOrZero(),
                productId = productId,
                shopId = shopId
            )
        )
        return CartTokoFoodParam(carts = cartList)
    }

    private fun getUnavailableProductsParam(): CartTokoFoodParam {
        val cartList = cartDataState.value.unavailableSection.products
            .asSequence()
            .map {
                CartItemTokoFoodParam(
                    cartId = it.cartId.toLongOrZero(),
                    productId = it.productId,
                    shopId = shopId
                )
            }.toList()
        return CartTokoFoodParam(carts = cartList)
    }

}
