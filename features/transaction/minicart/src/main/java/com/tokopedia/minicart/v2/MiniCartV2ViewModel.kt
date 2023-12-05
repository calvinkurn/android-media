package com.tokopedia.minicart.v2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartOccMultiDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartABTestData
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.domain.GetMiniCartParam
import com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MiniCartV2ViewModel @Inject constructor(
    private val executorDispatchers: CoroutineDispatchers,
    private val getMiniCartWidgetUseCase: GetMiniCartWidgetUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val addToCartOccMultiUseCase: AddToCartOccMultiUseCase
) : BaseViewModel(executorDispatchers.main) {

    private val _miniCartABTestData = MutableLiveData<MiniCartABTestData>()
    val miniCartABTestData: LiveData<MiniCartABTestData>
        get() = _miniCartABTestData

    private val _miniCartLoadingState = MutableLiveData<Boolean>()
    val miniCartLoadingState: LiveData<Boolean>
        get() = _miniCartLoadingState

    private val _globalEvent = MutableSharedFlow<MiniCartV2GlobalEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val globalEvent: SharedFlow<MiniCartV2GlobalEvent>
        get() = _globalEvent.asSharedFlow()

    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    fun updateMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
    }

    fun updateMiniCartLoadingState(isLoading: Boolean) {
        _miniCartLoadingState.value = isLoading
    }

    private fun getMiniCartItems(): List<MiniCartItem> {
        return miniCartSimplifiedData.value?.miniCartItems?.values?.toList() ?: emptyList()
    }

    fun setMiniCartABTestData(isOCCFlow: Boolean, buttonBuyWording: String) {
        _miniCartABTestData.value = MiniCartABTestData(
            isOCCFlow = isOCCFlow,
            buttonBuyWording = buttonBuyWording
        )
    }

    fun getLatestWidgetState(param: GetMiniCartParam) {
        launch {
            try {
                updateMiniCartLoadingState(true)
                val data = getMiniCartWidgetUseCase.invoke(param)
                setMiniCartABTestData(
                    isOCCFlow = data.miniCartWidgetData.isOCCFlow,
                    buttonBuyWording = data.miniCartWidgetData.buttonBuyWording
                )
                updateMiniCartLoadingState(false)
                updateMiniCartSimplifiedData(data)
            } catch (e: Exception) {
                updateMiniCartLoadingState(false)
                if (miniCartSimplifiedData.value != null) {
                    updateMiniCartSimplifiedData(miniCartSimplifiedData.value!!)
                } else {
                    updateMiniCartSimplifiedData(MiniCartSimplifiedData())
                }
                _globalEvent.tryEmit(MiniCartV2GlobalEvent.FailToLoadMiniCart(e))
            }
        }
    }

    fun goToCheckout() {
        if (miniCartABTestData.value?.isOCCFlow == true) {
            addToCartForCheckout()
        } else {
            updateCartForCheckout()
        }
    }

    private fun updateCartForCheckout() {
        val updateCartRequests = mutableListOf<UpdateCartRequest>()
        val allMiniCartItem = getMiniCartItems()
        allMiniCartItem.forEach {
            if (it is MiniCartItem.MiniCartItemProduct && !it.isError) {
                updateCartRequests.add(
                    UpdateCartRequest(
                        cartId = it.cartId,
                        quantity = it.quantity,
                        notes = it.notes
                    )
                )
            } else if (it is MiniCartItem.MiniCartItemBundleGroup && !it.isError) {
                it.products.values.forEach { product ->
                    updateCartRequests.add(
                        UpdateCartRequest(
                            cartId = product.cartId,
                            quantity = product.quantity,
                            notes = product.notes,
                            bundleInfo = BundleInfo(
                                bundleId = it.bundleId,
                                bundleGroupId = it.bundleGroupId,
                                bundleQty = it.bundleQuantity
                            )
                        )
                    )
                }
            }
        }
        updateCartUseCase.setParams(updateCartRequests)
        updateCartUseCase.execute(
            onSuccess = {
                onSuccessUpdateCartForCheckout(it)
            },
            onError = {
                onErrorUpdateCartForCheckout(it)
            }
        )
    }

    private fun onSuccessUpdateCartForCheckout(updateCartV2Data: UpdateCartV2Data) {
        if (updateCartV2Data.data.status) {
            _globalEvent.tryEmit(MiniCartV2GlobalEvent.SuccessGoToCheckout)
        } else {
            _globalEvent.tryEmit(
                MiniCartV2GlobalEvent.FailGoToCheckout(
                    data = MiniCartCheckoutData(
                        errorMessage = updateCartV2Data.data.error,
                        outOfService = updateCartV2Data.data.outOfService,
                        toasterAction = updateCartV2Data.data.toasterAction
                    )
                )
            )
        }
    }

    private fun onErrorUpdateCartForCheckout(throwable: Throwable) {
        _globalEvent.tryEmit(MiniCartV2GlobalEvent.FailGoToCheckout(throwable = throwable))
    }

    private fun addToCartForCheckout() {
        val addToCartParams = mutableListOf<AddToCartOccMultiCartParam>()
        getMiniCartItems().forEach { miniCartItem ->
            if (miniCartItem is MiniCartItem.MiniCartItemProduct && !miniCartItem.isError) {
                addToCartParams.add(
                    AddToCartOccMultiCartParam(
                        cartId = miniCartItem.cartId,
                        productId = miniCartItem.productId,
                        shopId = miniCartItem.shopId,
                        quantity = miniCartItem.quantity.toString(),
                        notes = miniCartItem.notes,
                        warehouseId = miniCartItem.warehouseId,
                        attribution = miniCartItem.attribution
                    )
                )
            }
        }
        val params = AddToCartOccMultiRequestParams(
            carts = addToCartParams,
            source = AddToCartOccMultiRequestParams.SOURCE_MINICART
        )
        addToCartOccMultiUseCase.setParams(params)

        addToCartOccMultiUseCase.execute(
            onSuccess = {
                onSuccessAddToCartForCheckout(it)
            },
            onError = {
                onErrorAddToCartForCheckout(it)
            }
        )
    }

    private fun onSuccessAddToCartForCheckout(
        addToCartOccMultiDataModel: AddToCartOccMultiDataModel
    ) {
        if (!addToCartOccMultiDataModel.isStatusError()) {
            _globalEvent.tryEmit(MiniCartV2GlobalEvent.SuccessGoToCheckout)
        } else {
            _globalEvent.tryEmit(
                MiniCartV2GlobalEvent.FailGoToCheckout(
                    data = MiniCartCheckoutData(
                        errorMessage = addToCartOccMultiDataModel.getAtcErrorMessage() ?: "",
                        outOfService = addToCartOccMultiDataModel.data.outOfService,
                        toasterAction = addToCartOccMultiDataModel.data.toasterAction
                    )
                )
            )
        }
    }

    private fun onErrorAddToCartForCheckout(throwable: Throwable) {
        _globalEvent.tryEmit(MiniCartV2GlobalEvent.FailGoToCheckout(throwable = throwable))
    }
}
