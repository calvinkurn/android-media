package com.tokopedia.tokofood.common.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokofood.common.domain.param.CartTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.CheckoutTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.usecase.LoadCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateCartTokoFoodUseCase
import com.tokopedia.tokofood.common.minicartwidget.domain.model.CartProduct
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel
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
                                                     private val updateCartTokoFoodUseCase: UpdateCartTokoFoodUseCase
) : ViewModel(), CoroutineScope {
    val inputFlow = MutableSharedFlow<String>(1)

    private val cartDataState = MutableStateFlow(CheckoutTokoFoodData())
    private val cartDataFlow = cartDataState.asStateFlow()

    private val _cartDataValidationState = MutableSharedFlow<Boolean>()
    val cartDataValidationState: SharedFlow<Boolean>
        get() = _cartDataValidationState

    companion object {
        const val INPUT_KEY = "string_input"
        const val MINI_CART_STATE_KEY = "mini_cart_state_key"
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

    ////// Mini Cart

//    private val miniCartState = MutableStateFlow(MiniCartUiModel())
//    val cartListFlow = MutableSharedFlow<Map<String, CartProduct>>(1)

    private val miniCartUiModelState = MutableStateFlow(MiniCartUiModel())
    val miniCartFlow = miniCartUiModelState.asStateFlow()

    fun loadCartList() {
        launchCatchError(block = {
            val loadCartParam = CheckoutTokoFoodParam()
            loadCartTokoFoodUseCase(loadCartParam).collect {
                cartDataState.value = it.data
            }
            cartDataFlow.collect {
                miniCartUiModelState.value = mapCartDataToMiniCart(it)
            }
        }, onError = {
            Timber.e(it)
        })
    }

    fun updateAndValidateCart() {
        launchCatchError(block = {
            val updateCartParam = CartTokoFoodParam()
            updateCartTokoFoodUseCase(updateCartParam).collect {
                if (it.success == 1) {
                    _cartDataValidationState.emit(true)
                }
            }
        }, onError = {
            Timber.e(it)
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

    fun updateCartList(product: CartProduct) {
        val tmpData = miniCartUiModelState.value
        tmpData.cartData[product.id] = product
        miniCartUiModelState.value = tmpData
    }

    fun deleteProduct(productId: String) {
        val tmpData = miniCartUiModelState.value
        tmpData.cartData.remove(productId)
        miniCartUiModelState.value = tmpData
    }

}
