package com.tokopedia.tokofood.common.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokofoodParam
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.CartListData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.usecase.AddToCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.LoadCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.MiniCartListTokofoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.RemoveCartTokofoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateQuantityTokofoodUseCase
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.common.util.getMiniCartState
import com.tokopedia.tokofood.common.util.setMiniCartState
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class MultipleFragmentsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loadCartTokoFoodUseCase: Lazy<LoadCartTokoFoodUseCase>,
    private val miniCartTokoFoodUseCase: Lazy<MiniCartListTokofoodUseCase>,
    private val addToCartTokoFoodUseCase: Lazy<AddToCartTokoFoodUseCase>,
    private val updateCartTokoFoodUseCase: Lazy<UpdateCartTokoFoodUseCase>,
    private val updateQuantityTokofoodUseCase: Lazy<UpdateQuantityTokofoodUseCase>,
    private val removeCartTokofoodUseCase: Lazy<RemoveCartTokofoodUseCase>,
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    private val cartDataState = MutableStateFlow<CartGeneralCartListData?>(null)
    val cartDataFlow = cartDataState.asStateFlow()

    private val cartDataValidationState = MutableStateFlow(UiEvent())
    val cartDataValidationFlow: StateFlow<UiEvent>
        get() = cartDataValidationState

    private val miniCartUiModelState =
        MutableStateFlow<Result<MiniCartUiModel>>(Result.Success(MiniCartUiModel()))
    val miniCartFlow = miniCartUiModelState.asStateFlow()

    private val _hasCartUpdatedIntoLatestState = MutableLiveData<Boolean>()
    val hasCartUpdatedIntoLatestState: LiveData<Boolean>
        get() = _hasCartUpdatedIntoLatestState

    val shopId: String
        get() {
            val cardDataValue = cartDataState.value
            val tokofoodBusinessData = cardDataValue?.data?.getTokofoodBusinessData()
            val shopData = tokofoodBusinessData?.customResponse?.shop
            val shopId = shopData?.shopId
            return shopId.orEmpty()
        }

    init {
        viewModelScope.launch {
            miniCartUiModelState.flatMapConcat { miniCartState ->
                flow {
                    val hasCartUpdated = miniCartState !is Result.Loading
                    emit(hasCartUpdated)
                }
            }.collect { hasCartUpdated ->
                _hasCartUpdatedIntoLatestState.value = hasCartUpdated
            }
        }
    }

    fun onSavedInstanceState() {
        val replayCache = miniCartUiModelState.replayCache.firstOrNull() as? Result.Success<MiniCartUiModel>
        val miniCartUiModel = replayCache?.data
        miniCartUiModel?.let {
            savedStateHandle.setMiniCartState(it)
        }
    }

    fun onRestoreSavedInstanceState() {
        miniCartUiModelState.tryEmit(
            Result.Success(savedStateHandle.getMiniCartState())
        )
    }

    fun loadInitial(source: String) {
        cartDataState.value.let { cartData ->
            if (cartData == null) {
                loadCartList(source)
            } else {
                launch(coroutineContext) {
                    val newCartData = cartData.copy()
                    cartDataState.emit(newCartData)
                    setMiniCartValue(newCartData)
                }
            }
        }
    }

    fun loadCartList(response: CartGeneralCartListData?) {
        launch(coroutineContext) {
            cartDataState.emit(response)
            setMiniCartValue(response)
        }
    }

    fun deleteProduct(cartId: String,
                      source: String,
                      shouldRefreshCart: Boolean = true) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            val removeCartParam = RemoveCartTokofoodParam.getProductParamById(cartId)
            withContext(dispatchers.io) {
                removeCartTokofoodUseCase.get().execute(removeCartParam)
            }.let {
                if (shouldRefreshCart) {
                    loadCartList(source)
                }
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_DELETE_PRODUCT,
                        source = source,
                        data = cartId
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_DELETE_PRODUCT,
                    source = source,
                    throwable = it
                )
            )
        })
    }

    fun deleteAllAtcAndAddProduct(updateParam: UpdateParam,
                                  source: String) {
        launchCatchError(block = {
            val removeCartParam = getRemoveAllProductParamByIdList()
            if (removeCartParam.getIsCartIdsEmpty()) {
                cartDataValidationState.emit(
                    UiEvent(state = UiEvent.EVENT_HIDE_LOADING_ADD_TO_CART)
                )
                withContext(dispatchers.io) {
                    removeCartTokofoodUseCase.get().execute(removeCartParam)
                }.let {
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
            val removeCartParam = getUnavailableProductsParam()
            if (removeCartParam.getIsCartIdsEmpty()) {
                cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
                withContext(dispatchers.io) {
                    removeCartTokofoodUseCase.get().execute(removeCartParam)
                }.let {
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

    fun updateNotes(updateParam: UpdateParam,
                    source: String,
                    shouldRefreshCart: Boolean = true) {
        launchCatchError(block = {
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_LOADING_DIALOG))
            withContext(dispatchers.io) {
                updateCartTokoFoodUseCase.get().execute(updateParam)
            }.let {
                if (shouldRefreshCart) {
                    loadCartList(source)
                }
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_NOTES,
                        source = source,
                        data = updateParam to it.data
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_UPDATE_NOTES,
                    source = source,
                    throwable = it
                )
            )
        })
    }

    fun updateQuantity(updateParam: UpdateParam,
                       source: String,
                       shouldRefreshCart: Boolean = true) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                updateCartTokoFoodUseCase.get().execute(updateParam)
            }.let {
                if (shouldRefreshCart) {
                    loadCartList(source)
                }
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY,
                        source = source,
                        data = updateParam to it.data
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_UPDATE_QUANTITY,
                    source = source,
                    throwable = it
                )
            )
        })
    }

    fun updateQuantity(updateParam: UpdateQuantityTokofoodParam,
                       source: String,
                       shouldRefreshCart: Boolean = true) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                updateQuantityTokofoodUseCase.get().execute(updateParam)
            }.let {
                if (shouldRefreshCart) {
                    loadCartList(source)
                }
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY_NEW,
                        source = source,
                        data = updateParam
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_UPDATE_QUANTITY,
                    source = source,
                    throwable = it
                )
            )
        })
    }

    fun updateCart(updateParam: UpdateParam,
                   source: String) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                updateCartTokoFoodUseCase.get().execute(updateParam)
            }.let {
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_HIDE_LOADING_UPDATE_TO_CART,
                        data = null
                    )
                )
                loadCartList(source)
                cartDataValidationState.emit(
                    UiEvent(
                        state = UiEvent.EVENT_SUCCESS_UPDATE_CART,
                        source = source,
                        data = updateParam to it.data
                    )
                )
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_UPDATE_CART,
                    source = source,
                    throwable = it
                )
            )
        })
    }

    fun addToCart(updateParam: UpdateParam,
                  source: String) {
        launchCatchError(block = {
            withContext(dispatchers.io) {
                addToCartTokoFoodUseCase.get().execute(updateParam)
            }.let {
                if (it.data.bottomSheet.isShowBottomSheet) {
                    cartDataValidationState.emit(
                        UiEvent(
                            state = UiEvent.EVENT_PHONE_VERIFICATION,
                            source = source,
                            data = it.data.bottomSheet
                        )
                    )
                } else {
                    cartDataValidationState.emit(
                        UiEvent(
                            state = UiEvent.EVENT_HIDE_LOADING_ADD_TO_CART,
                            data = null
                        )
                    )
                    loadCartList(source)
                    cartDataValidationState.emit(
                        UiEvent(
                            state = UiEvent.EVENT_SUCCESS_ADD_TO_CART,
                            source = source,
                            data = updateParam to it.data
                        )
                    )
                }
            }
        }, onError = {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_ADD_TO_CART,
                    source = source,
                    throwable = it
                )
            )
        })
    }

    fun clickMiniCart(source: String) {
        launch {
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT,
                    source = source,
                    data = cartDataFlow.value
                )
            )
        }
    }

    fun loadCartList(source: String) {
        launchCatchError(block = {
            miniCartUiModelState.emit(Result.Loading())
            withContext(dispatchers.io) {
                miniCartTokoFoodUseCase.get().execute(source)
            }.let {
                cartDataState.emit(it)
                setMiniCartValue(it)
            }
        }, onError = {
            miniCartUiModelState.emit(Result.Failure(it))
            cartDataValidationState.emit(
                UiEvent(
                    state = UiEvent.EVENT_FAILED_LOAD_CART,
                    throwable = it
                )
            )
        })
    }

    private suspend fun setMiniCartValue(data: CartGeneralCartListData?) {
        if (data == null) {
            miniCartUiModelState.emit(Result.Success(MiniCartUiModel()))
            cartDataValidationState.emit(UiEvent(state = UiEvent.EVENT_FAILED_LOAD_CART))
        } else {
            miniCartUiModelState.emit(Result.Success(data.getMiniCartUiModel()))
            val state =
                if (data.getShouldShowMiniCart()) {
                    UiEvent.EVENT_SUCCESS_LOAD_CART
                } else {
                    UiEvent.EVENT_FAILED_LOAD_CART
                }
            cartDataValidationState.emit(UiEvent(state = state))
        }
    }

    private fun getRemoveAllProductParamByIdList(): RemoveCartTokofoodParam {
        return cartDataState.value?.getRemoveAllCartParam() ?: RemoveCartTokofoodParam()
    }

    private fun getUnavailableProductsParam(): RemoveCartTokofoodParam {
        return cartDataState.value?.getRemoveUnavailableCartParam() ?: RemoveCartTokofoodParam()
    }

}
