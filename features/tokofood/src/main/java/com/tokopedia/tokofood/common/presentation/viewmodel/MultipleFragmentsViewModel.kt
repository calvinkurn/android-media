package com.tokopedia.tokofood.common.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.param.RemoveItemTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.usecase.AddToCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.LoadCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.RemoveCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateCartTokoFoodUseCase
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
class MultipleFragmentsViewModel @Inject constructor(val savedStateHandle: SavedStateHandle,
                                                     private val loadCartTokoFoodUseCase: LoadCartTokoFoodUseCase,
                                                     private val addToCartTokoFoodUseCase: AddToCartTokoFoodUseCase,
                                                     private val updateCartTokoFoodUseCase: UpdateCartTokoFoodUseCase,
                                                     private val removeCartTokoFoodUseCase: RemoveCartTokoFoodUseCase
) : ViewModel(), CoroutineScope {
    val inputFlow = MutableSharedFlow<String>(Int.ONE)

    private val cartDataState = MutableStateFlow(CheckoutTokoFoodData())
    val cartDataFlow = cartDataState.asStateFlow()

    private val cartDataValidationState = MutableSharedFlow<UiEvent>()
    val cartDataValidationFlow: SharedFlow<UiEvent>
        get() = cartDataValidationState

    private val miniCartUiModelState = MutableStateFlow<Result<MiniCartUiModel>>(Result.Success(MiniCartUiModel()))
    val miniCartFlow = miniCartUiModelState.asStateFlow()

    private val miniCartLoadingQueue = MutableLiveData(-Int.ONE)

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

    fun loadCartList(source: String) {
        launchCatchError(block = {
            miniCartLoadingQueue.value = miniCartLoadingQueue.value?.plus(Int.ONE)
            miniCartUiModelState.emit(Result.Loading())
            loadCartTokoFoodUseCase(source).collect {
                cartDataState.emit(it.data)
                miniCartLoadingQueue.value= miniCartLoadingQueue.value?.minus(Int.ONE)
                if (miniCartLoadingQueue.value?.isLessThanZero() == true) {
                    miniCartUiModelState.emit(Result.Success(mapCartDataToMiniCart(it.data)))
                }
                cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_SUCCESS_LOAD_CART))
            }
        }, onError = {
            miniCartLoadingQueue.value?.minus(Int.ONE)
            miniCartUiModelState.emit(Result.Failure(it))
            cartDataValidationState.emit(UiEvent(
                state = UiEvent.EVENT_FAILED_LOAD_CART,
                throwable = it
            ))
        })
    }

    fun loadCartList(response: CheckoutTokoFood) {
        cartDataState.value = response.data
    }

    fun deleteProduct(productId: String, cartId: String, source: String, shopId: String? = null) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            val paramShopId = shopId ?: this@MultipleFragmentsViewModel.shopId
            val removeCartParam = getProductParamById(productId, cartId, paramShopId)
            removeCartTokoFoodUseCase(removeCartParam).collect {
                if (it.isSuccess()) {
                    loadCartList(source)
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
                    state = UiEvent.EVENT_FAILED_DELETE_PRODUCT,
                    throwable = it
                )
            )
        })
    }

    fun deleteUnavailableProducts(source: String, shopId: String? = null) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            val paramShopId = shopId ?: this@MultipleFragmentsViewModel.shopId
            val removeCartParam = getUnavailableProductsParam(paramShopId)
            removeCartTokoFoodUseCase(removeCartParam).collect {
                if (it.isSuccess()) {
                    loadCartList(source)
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

    fun updateNotes(updateParam: UpdateParam, source: String) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            updateCartTokoFoodUseCase(updateParam).collect {
                if (it.isSuccess()) {
                    loadCartList(source)
                    cartDataValidationState.emit(
                        UiEvent(
                            state = UiEvent.EVENT_SUCCESS_UPDATE_NOTES,
                            data = updateParam to it.data
                        )
                    )
                }
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

    fun updateQuantity(updateParam: UpdateParam, source: String) {
        launchCatchError(block = {
            updateCartTokoFoodUseCase(updateParam).collect {
                if (it.isSuccess()) {
                    loadCartList(source)
                    cartDataValidationState.emit(UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY,
                        data = updateParam to it.data
                    ))
                }
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
            updateCartTokoFoodUseCase(updateParam).collect {
                if (it.isSuccess()) {
                    loadCartList(source)
                    cartDataValidationState.emit(UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_CART,
                        data = updateParam to it.data
                    ))
                }
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
            addToCartTokoFoodUseCase(updateParam).collect {
                if (it.isSuccess()) {
                    if (it.data.bottomSheet.isShowBottomSheet) {
                        cartDataValidationState.emit(UiEvent(
                            state = UiEvent.EVENT_PHONE_VERIFICATION,
                            data = it.data.bottomSheet)
                        )
                    } else {
                        loadCartList(source)
                        cartDataValidationState.emit(UiEvent(
                            state = UiEvent.EVENT_SUCCESS_ADD_TO_CART,
                            data = updateParam to it.data
                        ))
                    }
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

    private fun mapCartDataToMiniCart(cartData: CheckoutTokoFoodData): MiniCartUiModel {
        return MiniCartUiModel(
            shopName = cartData.shop.name,
            totalPrice = cartData.shoppingSummary.total.cost,
            totalPriceFmt = cartData.summaryDetail.totalPrice,
            totalProductQuantity = cartData.summaryDetail.totalItems
        )
    }

    private fun getProductParamById(productId: String, cartId: String, shopId: String): RemoveCartTokoFoodParam {
        val cartList = listOf(
            RemoveItemTokoFoodParam(
                cartId = cartId.toLongOrZero(),
                productId = productId,
                shopId = shopId
            )
        )
        return RemoveCartTokoFoodParam(carts = cartList)
    }

    private fun getUnavailableProductsParam(shopId: String): RemoveCartTokoFoodParam {
        val cartList = cartDataState.value.unavailableSection.products
            .asSequence()
            .map {
                RemoveItemTokoFoodParam(
                    cartId = it.cartId.toLongOrZero(),
                    productId = it.productId,
                    shopId = shopId
                )
            }.toList()
        return RemoveCartTokoFoodParam(carts = cartList)
    }

}
