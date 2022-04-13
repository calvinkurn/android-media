package com.tokopedia.tokofood.common.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokofood.common.domain.param.CartItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.domain.usecase.LoadCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.RemoveCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateCartTokoFoodUseCase
import com.tokopedia.tokofood.common.minicartwidget.domain.model.CartProduct
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel
import com.tokopedia.tokofood.common.presentation.UiEvent
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

    private val cartDataState = MutableStateFlow(CheckoutTokoFoodData())
    private val cartDataFlow = cartDataState.asStateFlow()

    // When user clicked on minicart Order button. If emit true, should go to checkout/purchase page
    private val cartDataValidationState = MutableSharedFlow<UiEvent>()
    val cartDataValidationFlow: SharedFlow<UiEvent>
        get() = cartDataValidationState

    private val miniCartUiModelState = MutableStateFlow(MiniCartUiModel())
    val miniCartFlow = miniCartUiModelState.asStateFlow()

    private val shopId: String
        get() = cartDataState.value.shop.shopId

    companion object {
        const val INPUT_KEY = "string_input"
        const val MINI_CART_STATE_KEY = "mini_cart_state_key"
    }

    init {
        viewModelScope.launch {
            cartDataFlow.collect {
                miniCartUiModelState.value = mapCartDataToMiniCart(it)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = viewModelScope.coroutineContext

    fun onSavedInstanceState() {
        savedStateHandle[INPUT_KEY] = inputFlow.replayCache.firstOrNull()
        savedStateHandle[MINI_CART_STATE_KEY] = miniCartUiModelState.replayCache.firstOrNull()
    }

    fun onRestoreSavednstanceState() {
        inputFlow.tryEmit(savedStateHandle[INPUT_KEY] ?: "")
        miniCartUiModelState.tryEmit(savedStateHandle[MINI_CART_STATE_KEY] ?: MiniCartUiModel())
    }

    fun loadCartList() {
        launchCatchError(block = {
            val loadCartParam = CheckoutTokoFoodParam()
            loadCartTokoFoodUseCase(loadCartParam).collect {
                cartDataState.value = it.data
            }
        }, onError = {
            Timber.e(it)
        })
    }

    fun loadCartList(response: CheckoutTokoFoodResponse) {
        cartDataState.value = response.data
    }

    fun updateAndValidateCart() {
        launchCatchError(block = {
            val updateCartParam = CartTokoFoodParam()
            updateCartTokoFoodUseCase(updateCartParam).collect {
                if (it.success == 1) {
                    cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT))
                }
            }
        }, onError = {
            Timber.e(it)
        })
    }

    fun updateCartList(product: CartProduct) {
        val tmpData = miniCartUiModelState.value
        tmpData.cartData[product.id] = product
        miniCartUiModelState.value = tmpData
    }

    fun deleteProduct(productId: String) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            val removeCartParam = getProductParamById(productId)
            removeCartTokoFoodUseCase(removeCartParam).collect {
                if (it.success == 1) {
                    loadCartList()
                    cartDataValidationState.emit(
                        UiEvent(
                            state = UiEvent.EVENT_SUCCESS_DELETE_PRODUCT,
                            data = it.data
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

    // TODO: Move to mapper
    private fun mapCartDataToMiniCart(cartData: CheckoutTokoFoodData): MiniCartUiModel {
        val products = cartData.availableSection.products
        val totalPrice = products.sumOf { it.price }
        return MiniCartUiModel(
            shopName = cartData.shop.name,
            totalPrice = totalPrice,
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

    private fun getProductParamById(productId: String): CartTokoFoodParam {
//        val cartList = cartDataState.value.availableSection.products
//            .asSequence()
//            .filter { it.productId == productId }
//            .map {
//                CartItemTokoFoodParam(
//                    cartId = it.cartId.toLongOrZero(),
//                    productId = it.productId,
//                    shopId = shopId
//                )
//            }
//            .toList()
//        // TODO: Add additional attributes
//        return CartTokoFoodParam(carts = cartList)
        // TODO: Remove dummy
        val cartList = listOf(
            CartItemTokoFoodParam(
                productId = productId,
                shopId = shopId
            )
        )
        return CartTokoFoodParam(carts = cartList)
    }

}
