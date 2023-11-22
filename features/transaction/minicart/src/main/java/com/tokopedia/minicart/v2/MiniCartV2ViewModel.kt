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
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.data.MiniCartABTestData
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.v2.domain.GetMiniCartParam
import com.tokopedia.minicart.v2.domain.GetMiniCartWidgetUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class MiniCartV2ViewModel @Inject constructor(
    private val executorDispatchers: CoroutineDispatchers,
    private val getMiniCartWidgetUseCase: GetMiniCartWidgetUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val addToCartOccMultiUseCase: AddToCartOccMultiUseCase
) : BaseViewModel(executorDispatchers.main) {

    // Global Data
    private val _currentPage = MutableLiveData<MiniCartAnalytics.Page>()
    val currentPage: LiveData<MiniCartAnalytics.Page>
        get() = _currentPage

    private val _miniCartABTestData = MutableLiveData<MiniCartABTestData>()
    val miniCartABTestData: LiveData<MiniCartABTestData>
        get() = _miniCartABTestData

    // Widget DATA
    private val _globalEvent = MutableLiveData<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    // Setter & Getter
    // Some of setter & getter here added to reduce complexity (due to nullability) so we can increase unit test coverage

    fun initializeGlobalState() {
        _globalEvent.value = GlobalEvent()
    }

    fun updateMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
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

    // API Call & Callback

    fun getLatestWidgetState(param: GetMiniCartParam) {
        launch {
            try {
                val data = getMiniCartWidgetUseCase.invoke(param)
                setMiniCartABTestData(
                    isOCCFlow = data.miniCartWidgetData.isOCCFlow,
                    buttonBuyWording = data.miniCartWidgetData.buttonBuyWording
                )
                _miniCartSimplifiedData.value = data
            } catch (t: Throwable) {
                if (miniCartSimplifiedData.value != null) {
                    _miniCartSimplifiedData.value = miniCartSimplifiedData.value
                } else {
                    _miniCartSimplifiedData.value = MiniCartSimplifiedData()
                }
            }
        }
    }

    fun goToCheckout(observer: Int) {
        if (miniCartABTestData.value?.isOCCFlow == true) {
            addToCartForCheckout(observer)
        } else {
            updateCartForCheckout(observer)
        }
    }

    private fun updateCartForCheckout(observer: Int) {
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
                onSuccessUpdateCartForCheckout(it, observer)
            },
            onError = {
                onErrorUpdateCartForCheckout(observer, it)
            }
        )
    }

    private fun onSuccessUpdateCartForCheckout(updateCartV2Data: UpdateCartV2Data, observer: Int) {
        if (updateCartV2Data.data.status) {
            _globalEvent.value = GlobalEvent(
                observer = observer,
                state = GlobalEvent.STATE_SUCCESS_TO_CHECKOUT
            )
        } else {
            _globalEvent.value = GlobalEvent(
                observer = observer,
                state = GlobalEvent.STATE_FAILED_TO_CHECKOUT,
                data = MiniCartCheckoutData(
                    errorMessage = updateCartV2Data.data.error,
                    outOfService = updateCartV2Data.data.outOfService,
                    toasterAction = updateCartV2Data.data.toasterAction
                )
            )
        }
    }

    private fun onErrorUpdateCartForCheckout(observer: Int, throwable: Throwable) {
        _globalEvent.value = GlobalEvent(
            observer = observer,
            state = GlobalEvent.STATE_FAILED_TO_CHECKOUT,
            throwable = throwable
        )
    }

    private fun addToCartForCheckout(observer: Int) {
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
                onSuccessAddToCartForCheckout(it, observer)
            },
            onError = {
                onErrorAddToCartForCheckout(it, observer)
            }
        )
    }

    private fun onSuccessAddToCartForCheckout(
        addToCartOccMultiDataModel: AddToCartOccMultiDataModel,
        observer: Int
    ) {
        if (!addToCartOccMultiDataModel.isStatusError()) {
            _globalEvent.value = GlobalEvent(
                observer = observer,
                state = GlobalEvent.STATE_SUCCESS_TO_CHECKOUT
            )
        } else {
            _globalEvent.value = GlobalEvent(
                observer = observer,
                state = GlobalEvent.STATE_FAILED_TO_CHECKOUT,
                data = MiniCartCheckoutData(
                    errorMessage = addToCartOccMultiDataModel.getAtcErrorMessage() ?: "",
                    outOfService = addToCartOccMultiDataModel.data.outOfService,
                    toasterAction = addToCartOccMultiDataModel.data.toasterAction
                )
            )
        }
    }

    private fun onErrorAddToCartForCheckout(throwable: Throwable, observer: Int) {
        _globalEvent.value = GlobalEvent(
            observer = observer,
            state = GlobalEvent.STATE_FAILED_TO_CHECKOUT,
            throwable = throwable
        )
    }
}
