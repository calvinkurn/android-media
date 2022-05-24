package com.tokopedia.minicart.common.widget.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartcommon.data.request.updatecart.BundleInfo
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.minicart.cartlist.uimodel.MiniCartListUiModel
import com.tokopedia.minicart.chatlist.MiniCartChatListUiModelMapper
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.domain.data.MiniCartCheckoutData
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.common.widget.GlobalEvent
import javax.inject.Inject

class MiniCartGeneralViewModel @Inject constructor(
    executorDispatchers: CoroutineDispatchers,
    private val getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase,
    private val getMiniCartListUseCase: GetMiniCartListUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val miniCartChatListUiModelMapper: MiniCartChatListUiModelMapper
) : BaseViewModel(executorDispatchers.main) {

    // Global Data
    private val _currentShopIds = MutableLiveData<List<String>>()
    val currentShopIds: LiveData<List<String>>
        get() = _currentShopIds
    var isShopDirectPurchase = false

    // TODO: Ask: Should default source be MiniCartSource.TokonowHome?
    var currentSource: MiniCartSource = MiniCartSource.TokonowHome

    // Widget Data
    private val _globalEvent = MutableLiveData<GlobalEvent>()
    val globalEvent: LiveData<GlobalEvent>
        get() = _globalEvent

    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData

    // Bottom Sheet Data
    private val _miniCartChatListBottomSheetUiModel = MutableLiveData<MiniCartListUiModel>()
    val miniCartChatListBottomSheetUiModel: LiveData<MiniCartListUiModel>
        get() = _miniCartChatListBottomSheetUiModel

    fun initializeShopIds(shopIds: List<String>) {
        _currentShopIds.value = shopIds
    }

    private fun getShopIds(): List<String> {
        return currentShopIds.value ?: emptyList()
    }

    fun initializeGlobalState() {
        _globalEvent.value = GlobalEvent()
    }

    fun updateMiniCartSimplifiedData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.value = miniCartSimplifiedData
    }

    private fun getMiniCartItems(): List<MiniCartItem> {
        // TODO: check if direct get from map is possible for changes
        return miniCartSimplifiedData.value?.miniCartItems?.values?.toList() ?: emptyList()
    }

    fun goToCheckout(observer: Int) {
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
            } else if (it is MiniCartItem.MiniCartItemBundle && !it.isError) {
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
                onSuccessUpdateCartForCheckout(observer, it)
            },
            onError = {
                onErrorUpdateCartForCheckout(observer, it)
            }
        )
    }

    private fun onSuccessUpdateCartForCheckout(observer: Int, updateCartV2Data: UpdateCartV2Data) {
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

    private fun onSuccessGetCartList(isFirstLoad: Boolean, miniCartData: MiniCartData) {
        if (isFirstLoad && miniCartData.data.outOfService.id.isNotBlank() && miniCartData.data.outOfService.id != "0") {
            _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_CHAT_BOTTOM_SHEET,
                data = miniCartData
            )
        } else {
            val tmpMiniCartChatListUiModel = miniCartChatListUiModelMapper.mapUiModel(miniCartData)

            tmpMiniCartChatListUiModel.isFirstLoad = isFirstLoad

            _miniCartChatListBottomSheetUiModel.value = tmpMiniCartChatListUiModel
        }
    }

    private fun onErrorGetCartList(isFirstLoad: Boolean, throwable: Throwable) {
        if (isFirstLoad) {
            _globalEvent.value = GlobalEvent(
                state = GlobalEvent.STATE_FAILED_LOAD_MINI_CART_CHAT_BOTTOM_SHEET,
                throwable = throwable
            )
        }
    }

    // API Call

    fun getLatestWidgetState(shopIds: List<String>? = null) {
        if (shopIds != null) {
            initializeShopIds(shopIds)
            getMiniCartListSimplifiedUseCase.setParams(shopIds, currentSource)
        } else {
            val tmpShopIds = getShopIds()
            getMiniCartListSimplifiedUseCase.setParams(tmpShopIds, currentSource)
        }
        getMiniCartListSimplifiedUseCase.execute(
            onSuccess = {
                _miniCartSimplifiedData.value = it
            },
            onError = {
                if (miniCartSimplifiedData.value != null) {
                    _miniCartSimplifiedData.value = miniCartSimplifiedData.value
                } else {
                    _miniCartSimplifiedData.value = MiniCartSimplifiedData()
                }
            }
        )
    }

    fun getCartList(isFirstLoad: Boolean = false) {
        val shopIds = getShopIds()
        getMiniCartListUseCase.setParams(shopIds)
        getMiniCartListUseCase.execute(
            onSuccess = {
                onSuccessGetCartList(isFirstLoad, it)
            },
            onError = {
                onErrorGetCartList(isFirstLoad, it)
            }
        )
    }
}