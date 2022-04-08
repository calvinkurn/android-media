package com.tokopedia.tokofood.common.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.tokofood.common.minicartwidget.domain.model.CartProduct
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel
import com.tokopedia.tokofood.common.util.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
class MultipleFragmentsViewModel(val savedStateHandle: SavedStateHandle) : ViewModel(), CoroutineScope {
    val inputFlow = MutableSharedFlow<String>(1)

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
    val miniCartFlow = miniCartUiModelState.shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    fun loadCartList() {
        // Todo : Load cart list
        launch {
            delay(1000) // Simulate hit API
            val isSuccess = true
            if (isSuccess) {
                // Todo : map response into CartProduct model
                val tmpCartMapData = hashMapOf<String, CartProduct>()
                tmpCartMapData["0"] = CartProduct()
                tmpCartMapData["1"] = CartProduct()
                tmpCartMapData["2"] = CartProduct()

                val shopName = ""
                val calculationResult = calculateTotal(tmpCartMapData)
                val totalProductQuantity = calculationResult.first
                val totalPrice = calculationResult.second


                val miniCartUiModel = MiniCartUiModel(
                        cartData = tmpCartMapData,
                        shopName = shopName,
                        totalPrice = totalPrice,
                        totalProductQuantity = totalProductQuantity
                )

                miniCartUiModelState.value = miniCartUiModel
            } else {

            }
        }
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
